package com.minhnha.textmessage.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.util.Result

class TMFakeRepository : DeviceConnectionRepository {

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
        _advertisingStatus.postValue(Result.Success)
    }

    override suspend fun startDiscovery() {
        _discoveryStatus.postValue(Result.Success)
    }

    override suspend fun stopAdvertising() {

    }

    override suspend fun stopDiscovery() {

    }

    override suspend fun acceptConnection(endpointId: String) {

    }

    override suspend fun rejectConnection(endpointId: String) {

    }

    override suspend fun sendData(endpointId: String, message: String) {

    }

    override suspend fun stopAllConnection() {

    }
}