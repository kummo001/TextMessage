package com.minhnha.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minhnha.domain.entity.Message


@Database(entities = [MessageEntity::class],version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao() : MessageDao
}