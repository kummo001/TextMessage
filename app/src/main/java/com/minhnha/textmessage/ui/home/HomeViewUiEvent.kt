package com.minhnha.textmessage.ui.home

sealed interface HomeViewUiEvent{
    class ScreenLoaded() : HomeViewUiEvent
}
