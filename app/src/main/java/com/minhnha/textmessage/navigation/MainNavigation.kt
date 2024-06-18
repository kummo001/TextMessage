package com.minhnha.textmessage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainView() {
    val navController = rememberNavController()
    NavigationHost(navController = navController)
}

@Composable
fun NavigationHost(navController: NavHostController){
    NavHost(navController = navController, startDestination = SplashRoute.Splash.Route){
        mainNavGraph(navController)
    }
}