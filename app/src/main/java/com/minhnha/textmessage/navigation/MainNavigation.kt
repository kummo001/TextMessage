package com.minhnha.textmessage.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainView() {
    val navController = rememberNavController()
    NavigationHost(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationHost(navController: NavHostController){
    NavHost(navController = navController, startDestination = SplashRoute.Splash.Route){
        mainNavGraph(navController)
    }
}