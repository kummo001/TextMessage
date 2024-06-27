package com.minhnha.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.minhnha.domain.entity.Message
import com.minhnha.domain.interfaces.MessageRepository
import com.minhnha.domain.repo.MessageFakeRepository
import com.minhnha.domain.usecase.message.DeleteMessageUseCase
import com.minhnha.domain.usecase.message.GetAllMessageUseCase
import com.minhnha.domain.usecase.message.InsertMessageUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeRepository: MessageRepository
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
    }

    @Test
    fun insertMessageUseCaseTest() {
        runTest {
            val message = Message(message = "Test message", endPointId = "AVNH", id = 1)
            val repository = fakeRepository as MessageFakeRepository
            insertMessageUseCase.invoke(message)
            assertThat(repository.list).contains(message)
        }
    }

    @Test
    fun deleteMessageUseCaseTest() {
        runTest {
            val message = Message(message = "Test message", endPointId = "AVNH", id = 1)
            val repository = fakeRepository as MessageFakeRepository
            insertMessageUseCase.invoke(message)
            assertThat(repository.list).contains(message)
            deleteMessageUseCase.invoke(Unit)
            assertThat(repository.list).doesNotContain(message)
        }
    }

    @Test
    fun getMessageUseCaseTest() {
        runTest {
            val message = Message(message = "Test message", endPointId = "AVNH", id = 1)
            val message2 = Message(message = "Test message 2", endPointId = "AVRH", id = 1)
            val repository = fakeRepository as MessageFakeRepository
            insertMessageUseCase.invoke(message)
            insertMessageUseCase.invoke(message2)
            assertThat(repository.list).contains(message)
            assertThat(repository.list).contains(message2)
        }
    }
}