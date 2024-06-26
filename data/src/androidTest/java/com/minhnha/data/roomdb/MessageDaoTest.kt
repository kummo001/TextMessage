package com.minhnha.data.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.minhnha.data.datasource.room.MessageDao
import com.minhnha.data.datasource.room.MessageDatabase
import com.minhnha.data.datasource.room.MessageEntity
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
class MessageDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("TestDatabase")
    lateinit var database: MessageDatabase
    private lateinit var dao: MessageDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.messageDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMessageTesting() = runTest {
        val message = MessageEntity(message = "Test message", endPointId = "AVNH", id = 1)
        dao.insertMessage(message = message)
        val list = dao.observeMessage()
        assertThat(list).contains(message)
    }

    @Test
    fun deleteMessagesTesting() = runTest {
        val message = MessageEntity(message = "Test message", endPointId = "AVNH", id = 1)
        dao.insertMessage(message = message)
        dao.deleteMessage()
        val list = dao.observeMessage()
        assertThat(list).doesNotContain(message)
    }
}