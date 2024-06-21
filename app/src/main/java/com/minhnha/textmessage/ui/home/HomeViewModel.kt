package com.minhnha.textmessage.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.nearby.connection.ConnectionInfo
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
    val showAlertDialogEvent: LiveData<Pair<String, ConnectionInfo>> =
        repository.showAlertDialogEvent

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
            repository.acceptConnection(endpointId)
        }
    }

    fun rejectConnection(endpointId: String) {
        viewModelScope.launch {
            repository.rejectConnection(endpointId)
        }
    }
}