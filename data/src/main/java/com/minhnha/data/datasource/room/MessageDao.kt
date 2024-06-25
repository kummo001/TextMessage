package com.minhnha.data.datasource.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.minhnha.domain.entity.Message
import kotlinx.coroutines.flow.StateFlow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM Message")
    suspend fun deleteMessage()

    @Query("SELECT * FROM Message")
    suspend fun observeMessage(): List<MessageEntity>
}