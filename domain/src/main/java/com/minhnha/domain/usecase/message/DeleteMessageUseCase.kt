package com.minhnha.domain.usecase.message

import com.minhnha.domain.interfaces.MessageRepository
import com.minhnha.domain.usecase.BaseUseCase
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(private val repository: MessageRepository) :
    BaseUseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        repository.deleteMessage()
    }
}