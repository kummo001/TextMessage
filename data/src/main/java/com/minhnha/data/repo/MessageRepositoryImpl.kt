package com.minhnha.data.repo

import com.minhnha.data.datasource.room.MessageDao
import com.minhnha.data.mapper.toMessage
import com.minhnha.data.mapper.toMessageEntity
import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {
    override suspend fun insertMessage(message: Message) {
        val messageEntity = message.toMessageEntity()
        messageDao.insertMessage(messageEntity)
    }

    override suspend fun deleteMessage() {
        messageDao.deleteMessage()
    }

    override suspend fun observeMessage(): List<Message> {
        val messageEntity = messageDao.observeMessage()
        return messageEntity.map { list -> list.toMessage() }
    }
}