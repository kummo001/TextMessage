package com.minhnha.data.di

import android.content.Context
import androidx.room.Room
import com.minhnha.data.datasource.room.MessageDao
import com.minhnha.data.datasource.room.MessageDatabase
import com.minhnha.data.repo.MessageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): MessageDatabase {
        return Room.databaseBuilder(context, MessageDatabase::class.java, "TextMessageDB").build()
    }

    @Provides
    fun provideDao(messageDatabase: MessageDatabase): MessageDao {
        return messageDatabase.messageDao()
    }
}