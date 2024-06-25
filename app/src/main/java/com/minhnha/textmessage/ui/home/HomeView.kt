package com.minhnha.textmessage.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.minhnha.domain.util.ConnectionStatus
import com.minhnha.domain.util.Result
import com.minhnha.textmessage.R
import com.minhnha.textmessage.navigation.MessageHistoryRoute
import com.minhnha.textmessage.theme.TextMessageTheme
import com.minhnha.textmessage.ui.composables.BaseButton
import com.minhnha.textmessage.ui.composables.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeView(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val advertisingState: State<Result> =
        viewModel.advertisingStatus.observeAsState(initial = Result.Empty)
    val discoveryState: State<Result> =
        viewModel.discoveryStatus.observeAsState(initial = Result.Empty)
    val connectionState: State<ConnectionStatus> =
        viewModel.connectionStatus.observeAsState(initial = ConnectionStatus.ConnectionRejected)
    val showAlertDialogEvent: State<Pair<String, ConnectionInfo>?> =
        viewModel.showAlertDialogEvent.observeAsState()
    val shouldShowDialog: State<Boolean> =
        viewModel.shouldShowDialog.observeAsState(initial = false)
    val focusManager = LocalFocusManager.current
    val receivedMessage: State<String> = viewModel.receivedMessage.observeAsState(initial = "")
    Log.d("TM", "Alert dialog event 1: ${showAlertDialogEvent.value?.first}")
    Log.d("TM", "Alert dialog event 2: ${showAlertDialogEvent.value?.second}")
    LaunchPermissionRequest(coroutine = coroutine, context = context)
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                val endpointId = showAlertDialogEvent.value?.first
                if (endpointId != null) {
                    viewModel.rejectConnection(endpointId)
                }
                viewModel.dismissDialog()
            },
            confirmButton = {
                val endpointId = showAlertDialogEvent.value?.first
                BaseButton(modifier = Modifier, isEnable = true, text = "OK") {
                    if (endpointId != null) {
                        viewModel.acceptConnection(endpointId)
                    }
                    viewModel.dismissDialog()
                }
            },
            dismissButton = {
                val endpointId = showAlertDialogEvent.value?.first
                BaseButton(modifier = Modifier, isEnable = true, text = "Cancel") {
                    if (endpointId != null) {
                        viewModel.rejectConnection(endpointId)
                    }
                    viewModel.dismissDialog()
                }
            },
            title = {
                val connectionInfo = showAlertDialogEvent.value?.second
                Text(
                    text = "Accept connection to ${connectionInfo?.endpointName} ?",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600
                    )
                )
            },
            text = {
                val connectionInfo = showAlertDialogEvent.value?.second
                Text(
                    text = "Confirm the code matches on both devices: + ${connectionInfo?.authenticationDigits}",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600
                    )
                )
            },
            containerColor = Color.White
        )
    }
    Scaffold(topBar = { TopBar() }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            HomeViewContent(
                advertisingState = advertisingState,
                discoveryState = discoveryState,
                receivedMessage = receivedMessage,
                connectionState = connectionState,
                connectionEvent = showAlertDialogEvent,
                onSendDataClick = { message ->
                    viewModel.sendData(message = message)
                },
                onHistoryButtonClick = {
                    navController.navigate(MessageHistoryRoute.MessageHistory.Route)
                },
                onStopConnectionClick = {
                    viewModel.stopAllConnection()
                },
                onStartAdvertising = {
                    viewModel.startAdvertising()
                },
                onStopAdvertising = {
                    viewModel.stopAdvertising()
                },
                onStartDiscovery = {
                    viewModel.startDiscovery()
                })
            {
                viewModel.stopDiscovery()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LaunchPermissionRequest(coroutine: CoroutineScope, context: Context) {
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
}

@Composable
fun HomeViewContent(
    advertisingState: State<Result>,
    discoveryState: State<Result>,
    receivedMessage: State<String>,
    connectionState: State<ConnectionStatus>,
    connectionEvent: State<Pair<String, ConnectionInfo>?>,
    onHistoryButtonClick: () -> Unit,
    onSendDataClick: (message: String) -> Unit,
    onStopConnectionClick: () -> Unit,
    onStartAdvertising: () -> Unit,
    onStopAdvertising: () -> Unit,
    onStartDiscovery: () -> Unit,
    onStopDiscovery: () -> Unit
) {
    val isAdvertising = remember {
        mutableStateOf(false)
    }

    val isDiscovery = remember {
        mutableStateOf(false)
    }

    val sendMessage = remember {
        mutableStateOf("")
    }
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
            text = "Status ",
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
            DeviceItem(connectionEvent.value?.second?.endpointName)
            Spacer(modifier = Modifier.height(10.dp))
            NearByStatus(
                advertisingState = advertisingState,
                discoveryState = discoveryState
            )
            Text(
                text = "Received message: ${receivedMessage.value}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = Color.Black
            )
            if (connectionState.value is ConnectionStatus.ConnectionOk) {
                TextField(
                    value = sendMessage.value,
                    onValueChange = {
                        sendMessage.value = it
                    },
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = TextFieldDefaults.colors().copy(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onSendDataClick(sendMessage.value) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF01347F),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Send data")
                    }

                    Spacer(modifier = Modifier.weight(0.1f))

                    Button(
                        onClick = { onStopConnectionClick() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Stop Connection", color = Color.Black)
                    }
                }
            } else {
                Text(
                    text = "Connection is disconnected ...",
                    style = TextStyle(color = Color.Black),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onHistoryButtonClick() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF01347F),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Message history", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isAdvertising.value) {
                Button(
                    onClick = {
                        onStopAdvertising()
                        isAdvertising.value = false
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Stop Advertising")
                }
            } else {
                Button(
                    onClick = {
                        onStartAdvertising()
                        isAdvertising.value = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    enabled = !isDiscovery.value,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF01347F),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Text(text = "Start Advertising")
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            if (isDiscovery.value) {
                Button(
                    onClick = {
                        onStopDiscovery()
                        isDiscovery.value = false
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Stop Discovery")
                }
            } else {
                Button(
                    onClick = {
                        onStartDiscovery()
                        isDiscovery.value = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    enabled = !isAdvertising.value,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF01347F),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Text(text = "Start Discovery")
                }
            }
        }
    }
}

@Composable
fun NearByStatus(
    advertisingState: State<Result>,
    discoveryState: State<Result>,
) {
    Text(
        text = when (advertisingState.value) {
            is Result.Success -> "Start advertising success"
            is Result.Empty -> "Not Advertising"
            is Result.Error -> "Start advertising fail ${(advertisingState.value as Result.Error).message}"
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = when (discoveryState.value) {
            is Result.Success -> "Start discovery success"
            is Result.Empty -> "Not Discovering"
            is Result.Error -> "Start discovery fail ${(discoveryState.value as Result.Error).message}"
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "Is advertising: ${advertisingState.value is Result.Success}",
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Is discovery: ${discoveryState.value is Result.Success}",
            color = Color.Black
        )
    }
}


@Composable
fun DeviceItem(text: String?) {
    if (!text.isNullOrBlank()) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .weight(5f),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    ),
                    maxLines = 1
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_phone_iphone_24),
                    contentDescription = "Phone",
                    modifier = Modifier
                        .size(40.dp)
                        .weight(1f)
                )
            }

        }
    }
}

@Preview
@Composable
fun HomeViewPreview() {
    TextMessageTheme {
//        HomeViewContent({}) {}
    }
}
