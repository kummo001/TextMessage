package com.minhnha.textmessage.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minhnha.textmessage.theme.TextMessageTheme

@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Scanned devices", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .border(
                    color = Color.Black,
                    width = 1.dp,
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            DeviceItem("Device 1")
            DeviceItem("Device 2")
            DeviceItem("Device 3")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Scan")
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Connect")
            }
        }
    }
}

@Composable
fun DeviceItem(text: String) {
    Text(text = text, Modifier.padding(horizontal = 10.dp))
}

@Preview
@Composable
fun HomeViewPreview() {
    TextMessageTheme {
        HomeView()
    }
}
