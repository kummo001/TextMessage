package com.minhnha.textmessage.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.minhnha.textmessage.navigation.HomeRoute
import kotlinx.coroutines.delay

@Composable
fun SplashView(navController: NavController) {
    LaunchedEffect(key1 = Unit){
        delay(1000)
        navController.popBackStack()
        navController.navigate(HomeRoute.Home.Route)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF01347F)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Text Me",
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color(0xFFFFFFFF), fontSize = 36.sp, fontWeight = FontWeight.W600)
        )
    }
}