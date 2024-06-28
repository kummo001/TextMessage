package com.minhnha.textmessage.ui.messagehistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.minhnha.domain.entity.Message
import com.minhnha.textmessage.theme.TextMessageTheme
import com.minhnha.textmessage.ui.composables.BaseButton
import com.minhnha.textmessage.ui.composables.TopBar

@Composable
fun MessageHistoryView() {
    val viewModel = hiltViewModel<MessageHistoryViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMessage()
    }
    val listMessage = uiState.value.listMessage
    Scaffold(topBar = { TopBar() }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            MessageHistoryContent(listMessage) {
                viewModel.deleteMessage()
            }
        }
    }
}

@Composable
fun MessageHistoryContent(
    listMessage: List<Message>,
    onDeleteHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFFFFFFF))
    ) {
        if (listMessage.isNotEmpty()) {
            listMessage.forEach {
                MessageItem(message = it.message, from = it.endPointId)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                isEnable = true,
                text = "Delete History"
            ) {
                onDeleteHistoryClick()
            }
        } else {
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = "Message history is empty",
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MessageItem(message: String, from: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Message: $message", color = Color.Black)
        Text(text = "From: $from", color = Color.Black)
    }
}

@Preview
@Composable
fun MessageHistoryViewPreview() {
    TextMessageTheme {
        MessageHistoryContent(mutableListOf()) {

        }
    }
}