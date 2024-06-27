package com.minhnha.textmessage.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.minhnha.domain.entity.Message
import com.minhnha.domain.usecase.message.DeleteMessageUseCase
import com.minhnha.domain.usecase.message.GetAllMessageUseCase
import com.minhnha.domain.usecase.message.InsertMessageUseCase
import com.minhnha.textmessage.MainCoroutineRule
import com.minhnha.textmessage.repo.MessageFakeRepository
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryUiState
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MessageHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeRepository: MessageFakeRepository

    private lateinit var viewModel: MessageHistoryViewModel

    private lateinit var deleteMessageUseCase: DeleteMessageUseCase

    private lateinit var getAllMessageUseCase: GetAllMessageUseCase

    private lateinit var insertMessageUseCase: InsertMessageUseCase

    @Before
    fun setUp() {
        //Test double
        fakeRepository = MessageFakeRepository()
        deleteMessageUseCase = DeleteMessageUseCase(fakeRepository)
        getAllMessageUseCase = GetAllMessageUseCase(fakeRepository)
        insertMessageUseCase = InsertMessageUseCase(fakeRepository)
        viewModel = MessageHistoryViewModel(
            deleteMessageUseCase,
            getAllMessageUseCase,
            insertMessageUseCase
        )
    }

    @Test
    fun insertMessageHistoryTest() {
        //assume repository work fine
        val msg = Message(id = 1, message = "Tower", endPointId = "Europe")
        runTest {
            val listOfEmittedMessage = mutableListOf<MessageHistoryUiState>()
            viewModel.insertMessage(msg)
            val job = launch {
                viewModel.uiState.toList(listOfEmittedMessage)
            }
            val list = viewModel.uiState.value
            assertThat(list.listMessage).contains(msg)
            job.cancel()
        }
    }

    @Test
    fun getMessageHistoryTest() {
        //assume repository work fine
        runTest {
            val msg = Message(id = 1, message = "Tower", endPointId = "Europe")
            val msg2 = Message(id = 2, message = "TowerOfGod", endPointId = "Japan")
            val listOfEmittedMessage = mutableListOf<MessageHistoryUiState>()
            viewModel.insertMessage(msg)
            viewModel.insertMessage(msg2)
            val job = launch {
                viewModel.uiState.toList(listOfEmittedMessage)
            }
            viewModel.getAllMessage()
            val list = viewModel.uiState.value
            assertThat(list.listMessage).contains(msg)
            assertThat(list.listMessage).contains(msg2)
            job.cancel()
        }
    }

    @Test
    fun deleteMessageHistoryTest() {
        //assume repository work fine
        val msg = Message(id = 1, message = "Tower", endPointId = "Europe")
        runTest {
            val listOfEmittedMessage = mutableListOf<MessageHistoryUiState>()
            viewModel.insertMessage(msg)
            val job = launch {
                viewModel.uiState.toList(listOfEmittedMessage)
            }
            val list = viewModel.uiState.value
            assertThat(list.listMessage).contains(msg)
            viewModel.deleteMessage()
            assertThat(list.listMessage).isEmpty()
            job.cancel()
        }
    }
}
