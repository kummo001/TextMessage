package com.minhnha.data.mapper

import com.minhnha.data.datasource.room.MessageEntity
import com.minhnha.domain.entity.Message

fun Message.toMessageEntity() = MessageEntity(
    id = id,
    message = message,
    endPointId = endPointId
)

fun MessageEntity.toMessage() = Message(
    id = id,
    message = message,
    endPointId = endPointId
)