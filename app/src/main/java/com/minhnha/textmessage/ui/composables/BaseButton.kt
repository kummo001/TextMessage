package com.minhnha.textmessage.ui.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BaseButton(
    modifier: Modifier,
    isEnable: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = isEnable,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = Color.White,
            disabledContentColor = Color.Black,
            containerColor = Color(0xFF01347F),
            disabledContainerColor = Color.Red
        )
    ) {
        Text(text = text)
    }
}