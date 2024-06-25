package com.minhnha.textmessage.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.usecase.message.InsertMessageUseCase
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deviceConnectionRepository: DeviceConnectionRepository,
    private val insertMessageUseCase: InsertMessageUseCase
) : ViewModel() {
    val advertisingStatus: LiveData<Result> = deviceConnectionRepository.advertisingStatus
    val discoveryStatus: LiveData<Result> = deviceConnectionRepository.discoveryStatus
    val connectionStatus: LiveData<ConnectionStatus> = deviceConnectionRepository.connectionStatus
    val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>> =
        deviceConnectionRepository.showAlertDialogEvent
    val receivedMessage: LiveData<String> = deviceConnectionRepository.receivedMessage
    private val _shouldShowDialog = MutableLiveData(false)
    private val mEndpointId = mutableStateOf("")
    val shouldShowDialog: LiveData<Boolean> = _shouldShowDialog

    init {
        showAlertDialogEvent.observeForever { newData ->
            if (newData != null) { // Optional check for null if needed
                _shouldShowDialog.value = true
            }
        }
        receivedMessage.observeForever {
            Log.d("TM", "onReceive message")
            receivedMessage.value?.let { message ->
                insertMessage(
                    message,
                    showAlertDialogEvent.value?.first ?: ""
                )
            }
        }
    }

    fun startAdvertising() {
        viewModelScope.launch {
            deviceConnectionRepository.startAdvertising()
        }
    }

    fun startDiscovery() {
        viewModelScope.launch {
            deviceConnectionRepository.startDiscovery()
        }
    }

    fun stopAdvertising() {
        viewModelScope.launch {
            deviceConnectionRepository.stopAdvertising()
        }
    }

    fun stopDiscovery() {
        viewModelScope.launch {
            deviceConnectionRepository.stopDiscovery()
        }
    }

    fun acceptConnection(endpointId: String) {
        viewModelScope.launch {
            mEndpointId.value = endpointId
            deviceConnectionRepository.acceptConnection(endpointId)
        }
    }

    fun rejectConnection(endpointId: String) {
        viewModelScope.launch {
            deviceConnectionRepository.rejectConnection(endpointId)
        }
    }

    fun sendData(message: String) {
        viewModelScope.launch {
            deviceConnectionRepository.sendData(mEndpointId.value, message)
        }
    }

    fun stopAllConnection() {
        viewModelScope.launch {
            deviceConnectionRepository.stopAllConnection()
        }
    }

    fun insertMessage(message: String, endpointId: String) {
        Log.d("TM","insert message")
        val messageObject = Message(
            endPointId = endpointId,
            message = message
        )
        viewModelScope.launch {
            insertMessageUseCase.invoke(messageObject)
        }
    }

    fun dismissDialog() {
        _shouldShowDialog.postValue(false)
    }
}