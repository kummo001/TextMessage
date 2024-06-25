package com.minhnha.textmessage.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minhnha.textmessage.ui.home.HomeView
import com.minhnha.textmessage.ui.messagehistory.MessageHistoryView
import com.minhnha.textmessage.ui.splash.SplashView

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    composable(route = SplashRoute.Splash.Route) {
        SplashView(navController)
    }
    composable(route = HomeRoute.Home.Route) {
        HomeView(navController)
    }
    composable(route = MessageHistoryRoute.MessageHistory.Route) {
        MessageHistoryView()
    }
}