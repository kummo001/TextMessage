package com.minhnha.textmessage.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.minhnha.domain.util.Result
import com.minhnha.textmessage.MainCoroutineRule
import com.minhnha.textmessage.getOrAwaitValueTest
import com.minhnha.textmessage.repo.TMFakeRepository
import com.minhnha.textmessage.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TextMessageViewModelUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        //Test double
        viewModel = HomeViewModel(TMFakeRepository())
    }

    @Test
    fun `start advertising success update live data`() {
        viewModel.startAdvertising()
        val value = viewModel.advertisingStatus.getOrAwaitValueTest()
        assertThat(value).isEqualTo(Result.Success)
    }

    @Test
    fun `start discovery success update live data`() {
        viewModel.startDiscovery()
        val value = viewModel.discoveryStatus.getOrAwaitValueTest()
        assertThat(value).isEqualTo(Result.Success)
    }
}