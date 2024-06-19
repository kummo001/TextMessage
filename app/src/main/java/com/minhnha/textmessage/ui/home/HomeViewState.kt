package com.minhnha.textmessage.ui.home

import com.minhnha.textmessage.core.TextMessageState

data class HomeViewState(override val bluetoothPermissionGranted: Boolean = false) :
    TextMessageState()