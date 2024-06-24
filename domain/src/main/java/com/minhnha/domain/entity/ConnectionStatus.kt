package com.minhnha.domain.entity

sealed class ConnectionStatus {
    data object ConnectionOk : ConnectionStatus()
    data object ConnectionRejected : ConnectionStatus()
    data class ConnectionError(val message: String) : ConnectionStatus()
}