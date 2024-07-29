package com.minhnha.textmessage.repo

import android.util.Log
import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import com.minhnha.textmessage.utils.Test.A
import kotlinx.coroutines.flow.MutableSharedFlow

class MessageFakeRepository: MessageRepository {
    private val list = mutableListOf<Message>()
    private val flow = MutableSharedFlow<Int>()
    suspend fun emit(value: Int) = flow.emit(value)

    override suspend fun insertMessage(message: Message) {
        list.add(message)
    }

    override suspend fun deleteMessage() {
        list.clear()
    }

    override suspend fun observeMessage(): List<Message> {
        return list
    }
}