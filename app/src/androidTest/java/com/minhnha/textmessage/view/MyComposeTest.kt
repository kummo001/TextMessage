package com.minhnha.textmessage.view

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.minhnha.domain.entity.Message
import com.minhnha.domain.util.Result
import com.minhnha.textmessage.MainActivity
import com.minhnha.textmessage.theme.TextMessageTheme
import com.minhnha.textmessage.ui.composables.TopBar
import com.minhnha.textmessage.ui.home.NearByStatus
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryContent
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryView
import com.minhnha.textmessage.ui.splash.SplashView
import org.junit.Rule
import org.junit.Test

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Test
    fun splashViewTest() {
        composeTestRule.activity.setContent {
            TextMessageTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                SplashView(navController = navController)
            }
        }
        composeTestRule.onNodeWithText("Text Me").assertExists()
    }

    @Test
    fun historyViewTest() {
        composeTestRule.activity.setContent {
            TextMessageTheme {
                MessageHistoryView()
            }
        }
        composeTestRule.onNodeWithText("Message history is empty").assertExists()
    }

    @Test
    fun topBarTest() {
        composeTestRule.activity.setContent {
            TextMessageTheme {
                TopBar()
            }
        }
        composeTestRule.onNodeWithText("Text Me").assertExists()
        composeTestRule.onNodeWithText("Text Me").performClick()
    }

    @Test
    fun nearByStatusViewSuccessTest() {
        val advertisingState = mutableStateOf(Result.Success)
        val discoveryState = mutableStateOf(Result.Success)
        composeTestRule.activity.setContent {
            TextMessageTheme {
                NearByStatus(advertisingState = advertisingState, discoveryState = discoveryState)
            }
        }
        composeTestRule.onNodeWithText("Start discovery success").assertExists()
        composeTestRule.onNodeWithText("Start advertising success").assertExists()
    }

    @Test
    fun nearByStatusViewEmptyTest() {
        val advertisingState = mutableStateOf(Result.Empty)
        val discoveryState = mutableStateOf(Result.Empty)
        composeTestRule.activity.setContent {
            TextMessageTheme {
                NearByStatus(advertisingState = advertisingState, discoveryState = discoveryState)
            }
        }
        composeTestRule.onNodeWithText("Not Advertising").assertExists()
        composeTestRule.onNodeWithText("Not Discovering").assertExists()
    }

    @Test
    fun nearByStatusViewFailTest() {
        val advertisingState = mutableStateOf(Result.Error("advertising cancel"))
        val discoveryState = mutableStateOf(Result.Error("discovery cancel"))
        composeTestRule.activity.setContent {
            TextMessageTheme {
                NearByStatus(advertisingState = advertisingState, discoveryState = discoveryState)
            }
        }
        composeTestRule.onNodeWithText("Start advertising fail advertising cancel").assertExists()
        composeTestRule.onNodeWithText("Start discovery fail discovery cancel").assertExists()
    }

    @Test
    fun messageHistoryContentListTest() {
        val msg1 = Message(id = 1, message = "John", endPointId = "1234")
        val msg2 = Message(id = 2, message = "Service", endPointId = "1111")
        val lisMessage = listOf(msg1, msg2)
        composeTestRule.activity.setContent {
            TextMessageTheme {
                MessageHistoryContent(listMessage = lisMessage) {

                }
            }
        }
        composeTestRule.onNodeWithText("Message: John").assertExists()
        composeTestRule.onNodeWithText("Message: Service").assertExists()
        composeTestRule.onNodeWithText("Delete History").assertExists()
        composeTestRule.onNodeWithText("Message history is empty").assertDoesNotExist()
    }

    @Test
    fun messageHistoryContentListEmptyTest() {
        val lisMessage = listOf<Message>()
        composeTestRule.activity.setContent {
            TextMessageTheme {
                MessageHistoryContent(listMessage = lisMessage) {

                }
            }
        }
        composeTestRule.onNodeWithText("Message history is empty").assertExists()
    }
}

