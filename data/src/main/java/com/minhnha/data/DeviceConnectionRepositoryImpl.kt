package com.minhnha.data

import android.util.Log
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceConnectionRepositoryImpl @Inject constructor(): DeviceConnectionRepository {
    override suspend fun scanDevices() {
        Log.d("TM","Scan device")
    }

    override suspend fun stopScanDevices() {
        Log.d("TM","stop scan device")
    }

    override suspend fun connectDevices() {
        Log.d("TM","connect device")
    }
}