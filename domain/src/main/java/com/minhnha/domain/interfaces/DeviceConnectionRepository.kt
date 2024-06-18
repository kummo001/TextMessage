package com.minhnha.domain.interfaces

interface DeviceConnectionRepository {
    suspend fun scanDevices()
    suspend fun stopScanDevices()
    suspend fun connectDevices()
}