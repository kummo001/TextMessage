package com.minhnha.textmessage


import com.minhnha.domain.interfaces.DeviceConnectionRepository
import com.minhnha.domain.util.Result
import com.minhnha.textmessage.ui.home.HomeViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class TextMessageViewModelUnitTest {

    private lateinit var viewModel: HomeViewModel

    @Inject
    private lateinit var repository: DeviceConnectionRepository

    @Before
    fun setUp() {
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `start advertising success update live data`() {

        val mockData = Result.Success
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}