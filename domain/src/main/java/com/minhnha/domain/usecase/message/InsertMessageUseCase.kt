package com.minhnha.domain.usecase.message

import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import com.minhnha.domain.usecase.BaseUseCase
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(private val repository: MessageRepository) :
    BaseUseCase<Message, Unit>() {
    override suspend fun execute(parameters: Message) {
        repository.insertMessage(parameters)
    }
}