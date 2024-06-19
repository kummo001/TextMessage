package com.minhnha.domain.interfaces

interface DeviceConnectionRepository {
    suspend fun startAdvertising()
    suspend fun startDiscovery()
    suspend fun connectDevices()
}