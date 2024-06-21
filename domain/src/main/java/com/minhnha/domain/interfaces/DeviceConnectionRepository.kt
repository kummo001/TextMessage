package com.minhnha.domain.interfaces

import androidx.lifecycle.LiveData
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.util.Result

interface DeviceConnectionRepository {
    val advertisingStatus: LiveData<Result>
    val discoveryStatus: LiveData<Result>
    val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>>
    suspend fun startAdvertising()
    suspend fun startDiscovery()
    suspend fun stopAdvertising()
    suspend fun stopDiscovery()
    suspend fun acceptConnection(endpointId: String)
    suspend fun rejectConnection(endpointId: String)
    suspend fun connectDevices()
}