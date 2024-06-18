package com.minhnha.textmessage.ui.home

import androidx.lifecycle.ViewModel
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DeviceConnectionRepository) :
    ViewModel() {
    suspend fun startScan() {
        repository.scanDevices()
    }
}