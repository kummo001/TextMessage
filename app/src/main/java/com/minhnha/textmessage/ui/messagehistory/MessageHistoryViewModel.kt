package com.minhnha.textmessage.ui.messagehistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhnha.domain.entity.Message
import com.minhnha.domain.usecase.message.DeleteMessageUseCase
import com.minhnha.domain.usecase.message.GetAllMessageUseCase
import com.minhnha.domain.usecase.message.InsertMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

data class MessageHistoryUiState(
    val listMessage: List<Message>
) {
    companion object {
        val default = MessageHistoryUiState(
            listMessage = mutableListOf()
        )
    }
}

@HiltViewModel
class MessageHistoryViewModel @Inject constructor(
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val getAllMessageUseCase: GetAllMessageUseCase,
    private val insertMessageUseCase: InsertMessageUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(MessageHistoryUiState.default)
    val uiState: StateFlow<MessageHistoryUiState>
        get() = _uiState

    fun deleteMessage() {
        viewModelScope.launch {
            deleteMessageUseCase.invoke(Unit)
            getAllMessage()
        }
    }

    fun getAllMessage() {
        viewModelScope.launch {
            val list = getAllMessageUseCase.invoke(Unit)
            _uiState.update { currentState -> currentState.copy(listMessage = list) }
        }
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch {
            insertMessageUseCase.invoke(message)
            getAllMessage()
        }
    }
}