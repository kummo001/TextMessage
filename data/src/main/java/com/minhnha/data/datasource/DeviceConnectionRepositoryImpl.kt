package com.minhnha.data.datasource

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceConnectionRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DeviceConnectionRepository {

    private val SERVICE_ID = "com.minhnha.textmessage.SERVICE_ID"
    override suspend fun startAdvertising() {
        Log.d("TM", "Start Advertising")
        val advertisingOption =
            AdvertisingOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startAdvertising("MinhNHA", SERVICE_ID, connectionLifecycleCallback, advertisingOption)
            .addOnSuccessListener {
                Log.d("TM", "Success advertising")
            }.addOnFailureListener { e ->
                Log.d("TM", "Fail advertising: ${e.message}")
            }
    }

    override suspend fun startDiscovery() {
        Log.d("TM", "Start discovery")
        val discoveryOptions =
            DiscoveryOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener {
                Log.d("TM", "Success discovery")
            }.addOnFailureListener { e ->
                Log.d("TM", "Fail discovery: ${e.message}")
            }
    }

    override suspend fun connectDevices() {
        Log.d("TM", "connect device")
    }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                AlertDialog.Builder(context)
                    .setTitle("Accept connection to " + connectionInfo.endpointName)
                    .setMessage("Confirm the code matches on both devices: " + connectionInfo.authenticationDigits)
                    .setPositiveButton("Accept") { _, _ ->
                        Nearby.getConnectionsClient(context)
                            .acceptConnection(endpointId, mPayloadCallback)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        Nearby.getConnectionsClient(context)
                            .rejectConnection(endpointId)
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {}
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {}
                    ConnectionsStatusCodes.STATUS_ERROR -> {}
                    else -> {}
                }
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
            }
        }

    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(context)
                    .requestConnection("MinhNHA", endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener { }
                    .addOnFailureListener { e: Exception? ->
                        Log.d("TM", "Fail discovery: ${e?.message}")
                    }
            }

            override fun onEndpointLost(endpointId: String) {
                // A previously discovered endpoint has gone away.
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
}