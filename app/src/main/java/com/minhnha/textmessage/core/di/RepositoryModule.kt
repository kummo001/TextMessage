package com.minhnha.textmessage.core.di

import com.minhnha.data.repo.DeviceConnectionRepositoryImpl
import com.minhnha.data.repo.MessageRepositoryImpl
import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.interfaces.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeviceConnectionRepository(impl: DeviceConnectionRepositoryImpl): DeviceConnectionRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}