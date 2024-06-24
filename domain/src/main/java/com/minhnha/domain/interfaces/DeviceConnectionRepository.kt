package com.minhnha.domain.interfaces

import androidx.lifecycle.LiveData
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.util.Result

interface DeviceConnectionRepository {
    val advertisingStatus: LiveData<Result>
    val discoveryStatus: LiveData<Result>
    val connectionStatus: LiveData<ConnectionStatus>
    val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>>
    val receivedMessage: LiveData<String>
    suspend fun startAdvertising()
    suspend fun startDiscovery()
    suspend fun stopAdvertising()
    suspend fun stopDiscovery()
    suspend fun acceptConnection(endpointId: String)
    suspend fun rejectConnection(endpointId: String)
    suspend fun sendData(endpointId: String, message: String)
    suspend fun stopAllConnection()
}