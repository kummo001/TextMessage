package com.minhnha.data.datasource

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.util.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceConnectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :
    DeviceConnectionRepository {

    private val SERVICE_ID = "com.minhnha.textmessage.SERVICE_ID"

    private val _advertisingStatus = MutableLiveData<Result>()
    override val advertisingStatus: LiveData<Result> = _advertisingStatus
    private val _discoveryStatus = MutableLiveData<Result>()
    override val discoveryStatus: LiveData<Result> = _discoveryStatus
    private val _showAlertDialogEvent = MutableLiveData<Pair<String, ConnectionInfo>>()
    override val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>> =
        _showAlertDialogEvent


    override suspend fun startAdvertising() {
        Log.d("TM", "Start Advertising")
        val advertisingOption =
            AdvertisingOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startAdvertising(
                getNickname(),
                SERVICE_ID,
                connectionLifecycleCallback,
                advertisingOption
            )
            .addOnSuccessListener {
                Log.d("TM", "Success startAdvertising")
                _advertisingStatus.postValue(Result.Success)
            }.addOnFailureListener { e ->
                _advertisingStatus.postValue(Result.Error("${e.message}"))
            }
    }

    override suspend fun startDiscovery() {
        Log.d("TM", "Start discovery")
        val discoveryOptions =
            DiscoveryOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener {
                Log.d("TM", "Success startDiscovery")
                _discoveryStatus.postValue(Result.Success)
            }.addOnFailureListener { e ->
                _discoveryStatus.postValue(Result.Error("${e.message}"))
            }
    }

    override suspend fun stopAdvertising() {
        Nearby.getConnectionsClient(context).stopAdvertising()
    }

    override suspend fun stopDiscovery() {
        Nearby.getConnectionsClient(context).stopDiscovery()
    }

    override suspend fun acceptConnection(endpointId: String) {
        Nearby.getConnectionsClient(context)
            .acceptConnection(endpointId, mPayloadCallback)
    }

    override suspend fun rejectConnection(endpointId: String) {
        Nearby.getConnectionsClient(context)
            .rejectConnection(endpointId)
    }

    override suspend fun connectDevices() {
        Log.d("TM", "connect device")
    }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                Log.d("TM", "On connection initiated")
                // Automatically accept the connection on both sides.
                _showAlertDialogEvent.postValue(Pair(endpointId, connectionInfo))
//                AlertDialog.Builder(
//                    ContextThemeWrapper(
//                        context,
//                        R.style.MyAppCompatDialog
//                    )
//                )
//                    .setTitle("Accept connection to " + connectionInfo.endpointName)
//                    .setMessage("Confirm the code matches on both devices: " + connectionInfo.authenticationDigits)
//                    .setPositiveButton("Accept") { _, _ ->
//                        Nearby.getConnectionsClient(context)
//                            .acceptConnection(endpointId, mPayloadCallback)
//                    }
//                    .setNegativeButton("Cancel") { _, _ ->
//                        Nearby.getConnectionsClient(context)
//                            .rejectConnection(endpointId)
//                    }
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show()
//                Nearby.getConnectionsClient(context)
//                    .acceptConnection(endpointId, mPayloadCallback)
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        Log.d("TM", "Connection status ok")
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        Log.d("TM", "Connection status rejected")
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        Log.d("TM", "Connection status error")
                    }

                    else -> {
                        Log.d("TM", "Unknown")
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
                Log.d("TM", "Disconnect from endpoint")
            }
        }

    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                // An endpoint was found. We request a connection to it.
                Log.d("TM", "On endpoint found")
                Nearby.getConnectionsClient(context)
                    .requestConnection(getNickname(), endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener { }
                    .addOnFailureListener { e: Exception? ->
                        Log.d("TM", "Fail discovery: ${e?.message}")
                    }
            }

            override fun onEndpointLost(endpointId: String) {
                // A previously discovered endpoint has gone away.
                Log.d("TM", "On endpoint lost")
            }
        }

    private val mPayloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Log.d("TM", "Payload received")
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d("TM", "Payload transfer update")
        }
    }

    private fun getNickname() = UUID.randomUUID().toString()
}