package com.minhnha.data.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM Message")
    suspend fun deleteMessage()

    @Query("SELECT * FROM Message")
    suspend fun observeMessage(): List<MessageEntity>
}