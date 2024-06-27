package com.minhnha.data.repo

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
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.util.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceConnectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceConnectionRepository {

    private val SERVICE_ID = "com.minhnha.textmessage.SERVICE_ID"

    private val _advertisingStatus = MutableLiveData<Result>()
    override val advertisingStatus: LiveData<Result>
        get() = _advertisingStatus

    private val _discoveryStatus = MutableLiveData<Result>()
    override val discoveryStatus: LiveData<Result>
        get() = _discoveryStatus

    private val _connectionStatus = MutableLiveData<ConnectionStatus>()
    override val connectionStatus: LiveData<ConnectionStatus>
        get() = _connectionStatus

    private val _showAlertDialogEvent = MutableLiveData<Pair<String, ConnectionInfo>>()
    override val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>>
        get() = _showAlertDialogEvent

    private val _receivedMessage = MutableLiveData<String>()
    override val receivedMessage: LiveData<String>
        get() = _receivedMessage


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
        _advertisingStatus.postValue(Result.Empty)
    }

    override suspend fun stopDiscovery() {
        Nearby.getConnectionsClient(context).stopDiscovery()
        _discoveryStatus.postValue(Result.Empty)
    }

    override suspend fun acceptConnection(endpointId: String) {
        Nearby.getConnectionsClient(context)
            .acceptConnection(endpointId, mPayloadCallback)
    }

    override suspend fun rejectConnection(endpointId: String) {
        Nearby.getConnectionsClient(context)
            .rejectConnection(endpointId)
    }

    override suspend fun sendData(endpointId: String, message: String) {
        sendPayload(endpointId, message)
    }

    override suspend fun stopAllConnection() {
        Nearby.getConnectionsClient(context)
            .stopAllEndpoints()
    }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                Log.d("TM", "On connection initiated")
                _showAlertDialogEvent.postValue(Pair(endpointId, connectionInfo))
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        Log.d("TM", "Connection status ok, automatically send an data now")
                        _connectionStatus.postValue(ConnectionStatus.ConnectionOk)
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        _connectionStatus.postValue(ConnectionStatus.ConnectionRejected)
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        _connectionStatus.postValue(ConnectionStatus.ConnectionError("Status error"))
                    }

                    else -> {
                        _connectionStatus.postValue(ConnectionStatus.ConnectionError("Unknown error"))
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
                _connectionStatus.postValue(ConnectionStatus.ConnectionError("Endpoint lost"))
            }
        }

    private val mPayloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedBytes = payload.asBytes()
            val receivedString = receivedBytes?.toString(Charsets.UTF_8)
            _receivedMessage.postValue(receivedString)
            Log.d("TM", "Payload received")
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d("TM", "Payload transfer update")
            if (update.status == PayloadTransferUpdate.Status.SUCCESS) {
                Log.d("TM", "Send data success")
            }
        }
    }

    private fun sendPayload(endPointId: String, message: String) {
        Log.d("TM", "Start send payload")
        val bytesPayload = Payload.fromBytes(message.toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(endPointId, bytesPayload)
            .addOnSuccessListener {
                Log.d("TM", "Send payload success")
            }
            .addOnFailureListener {
                Log.d("TM", "Send payload fail")
            }
    }

    private fun getNickname() = UUID.randomUUID().toString()
}