package com.minhnha.textmessage.view

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.minhnha.textmessage.MainActivity
import com.minhnha.textmessage.theme.TextMessageTheme
import com.minhnha.textmessage.ui.composables.TopBar
import com.minhnha.textmessage.ui.home.HomeView
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryView
import com.minhnha.textmessage.ui.splash.SplashView
import org.junit.Rule
import org.junit.Test

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: TestNavHostController

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
    fun homeViewTest() {
        composeTestRule.activity.setContent {
            TextMessageTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                HomeView(navController = navController)
            }
        }
        composeTestRule.onNodeWithTag("StartAdvertising").assertExists().performClick()
    }
}

