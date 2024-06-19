package com.minhnha.textmessage.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.minhnha.textmessage.R
import com.minhnha.textmessage.theme.TextMessageTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val check =
        context.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
    val multiplePermissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.NEARBY_WIFI_DEVICES,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
        )
    LaunchedEffect(key1 = multiplePermissionState) {
        if (!multiplePermissionState.allPermissionsGranted) {
            Log.d("TM", "All permission not granted")
            coroutine.launch {
                multiplePermissionState.launchMultiplePermissionRequest()
            }
        } else {
            Log.d("TM", "All permission granted")
        }
        Log.d("TM", "is access wifi state granted: $check")

    }
    Scaffold(topBar = { TopBar() }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            HomeViewContent(onStartAdvertisingClick = {
                coroutine.launch {
                    viewModel.startAdvertising()
                }
            }) {
                coroutine.launch {
                    viewModel.startDiscovery()
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF01347F))
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Text Me",
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color(0xFFFFFFFF),
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
        )
    }
}

@Composable
fun HomeViewContent(
    onStartAdvertisingClick: () -> Unit,
    onStartDiscoveryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = "Scanned devices: ",
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )
        )
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
                onClick = { onStartAdvertisingClick() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Start Advertising")
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                onClick = { onStartDiscoveryClick() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Start Discovery")
            }
        }
    }
}


@Composable
fun DeviceItem(text: String) {
    Card(
        onClick = { /*TODO*/ },
        border = BorderStroke(width = 1.dp, color = Color.Blue),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Gray
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.baseline_phone_iphone_24),
                contentDescription = "Phone",
                modifier = Modifier.size(40.dp)
            )
        }

    }
}

@Preview
@Composable
fun HomeViewPreview() {
    TextMessageTheme {
        HomeViewContent({}) {}
    }
}
