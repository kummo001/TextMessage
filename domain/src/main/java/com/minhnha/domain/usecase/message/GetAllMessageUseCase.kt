package com.minhnha.domain.usecase.message

import androidx.lifecycle.LiveData
import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import com.minhnha.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAllMessageUseCase @Inject constructor(private val repository: MessageRepository) :
    BaseUseCase<Unit, List<Message>>() {
    override suspend fun execute(parameters: Unit): List<Message> {
        return repository.observeMessage()
    }
}