package com.minhnha.textmessage.ui.home

import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.textmessage.core.TextMessageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DeviceConnectionRepository) :
    TextMessageViewModel<HomeViewState>(HomeViewState()) {
    override val _viewStateCopy: HomeViewState = viewState.copy()

    //Temp
    fun onUiEvent(uiEvent: HomeViewUiEvent) {
        when (uiEvent) {
            is HomeViewUiEvent.ScreenLoaded -> handleScan()
        }
    }

    private fun handleScan() = launch {
        startAdvertising()
    }

    suspend fun startAdvertising() {
        repository.startAdvertising()
    }

    suspend fun startDiscovery() {
        repository.startDiscovery()
    }
}