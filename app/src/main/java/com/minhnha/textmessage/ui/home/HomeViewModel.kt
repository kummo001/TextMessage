package com.minhnha.textmessage.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DeviceConnectionRepository) :
    ViewModel() {
    val advertisingStatus: LiveData<Result> = repository.advertisingStatus
    val discoveryStatus: LiveData<Result> = repository.discoveryStatus
    val connectionStatus: LiveData<ConnectionStatus> = repository.connectionStatus
    val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>> =
        repository.showAlertDialogEvent
    val receivedMessage: LiveData<String> = repository.receivedMessage
    private val _shouldShowDialog = MutableLiveData(false)
    private val mEndpointId = mutableStateOf("")
    val shouldShowDialog: LiveData<Boolean> = _shouldShowDialog

    init {
        showAlertDialogEvent.observeForever { newData ->
            if (newData != null) { // Optional check for null if needed
                _shouldShowDialog.value = true
            }
        }
    }

    fun startAdvertising() {
        viewModelScope.launch {
            repository.startAdvertising()
        }
    }

    fun startDiscovery() {
        viewModelScope.launch {
            repository.startDiscovery()
        }
    }

    fun stopAdvertising() {
        viewModelScope.launch {
            repository.stopAdvertising()
        }
    }

    fun stopDiscovery() {
        viewModelScope.launch {
            repository.stopDiscovery()
        }
    }

    fun acceptConnection(endpointId: String) {
        viewModelScope.launch {
            mEndpointId.value = endpointId
            repository.acceptConnection(endpointId)
        }
    }

    fun rejectConnection(endpointId: String) {
        viewModelScope.launch {
            repository.rejectConnection(endpointId)
        }
    }

    fun sendData(message: String) {
        viewModelScope.launch {
            repository.sendData(mEndpointId.value, message)
        }
    }

    fun stopAllConnection() {
        viewModelScope.launch {
            repository.stopAllConnection()
        }
    }

    fun dismissDialog() {
        _shouldShowDialog.postValue(false)
    }
}