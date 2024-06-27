package com.minhnha.domain.repo

import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class MessageFakeRepository : MessageRepository {
    val list = mutableListOf<Message>()

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