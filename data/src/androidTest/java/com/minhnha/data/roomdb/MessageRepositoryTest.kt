package com.minhnha.data.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.minhnha.data.datasource.room.MessageDao
import com.minhnha.data.datasource.room.MessageDatabase
import com.minhnha.data.datasource.room.MessageEntity
import com.minhnha.data.mapper.toMessage
import com.minhnha.data.repo.MessageRepositoryImpl
import com.minhnha.domain.interfaces.MessageRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MessageRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("TestDatabase")
    lateinit var database: MessageDatabase
    private lateinit var dao: MessageDao
    private lateinit var repository: MessageRepository

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.messageDao()
        repository = MessageRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMessageTesting() = runTest {
        val message = MessageEntity(message = "Test message", endPointId = "AVNH", id = 1)
        repository.insertMessage(message = message.toMessage())
        val list = dao.observeMessage()
        assertThat(list).contains(message)
    }

    @Test
    fun getMessagesTesting() = runTest {
        val message = MessageEntity(message = "Test message", endPointId = "AVNH", id = 1)
        val message2 = MessageEntity(message = "Test message 2", endPointId = "AVBH", id = 2)
        repository.insertMessage(message = message.toMessage())
        repository.insertMessage(message = message2.toMessage())
        val list = dao.observeMessage()
        assertThat(list).contains(message)
        assertThat(list).contains(message2)
    }

    @Test
    fun deleteMessagesTesting() = runTest {
        val message = MessageEntity(message = "Test message", endPointId = "AVNH", id = 1)
        repository.insertMessage(message = message.toMessage())
        repository.deleteMessage()
        val list = dao.observeMessage()
        assertThat(list).doesNotContain(message)
    }
}