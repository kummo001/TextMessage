package com.minhnha.textmessage.repo

import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository

class MessageFakeRepository: MessageRepository {
    override suspend fun insertMessage(message: Message) {
        //
    }

    override suspend fun deleteMessage() {
        //
    }

    override suspend fun observeMessage(): List<Message> {
        return mutableListOf()
    }
}