package com.minhnha.domain.interfaces

import androidx.lifecycle.LiveData
import com.minhnha.domain.entity.Message
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    suspend fun insertMessage(message: Message)
    suspend fun deleteMessage()
    suspend fun observeMessage(): List<Message>
}