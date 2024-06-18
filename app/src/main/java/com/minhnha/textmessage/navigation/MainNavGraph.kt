package com.minhnha.textmessage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minhnha.textmessage.ui.home.HomeView
import com.minhnha.textmessage.ui.splash.SplashView

fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    composable(route = SplashRoute.Splash.Route) {
        SplashView(navController)
    }
    composable(route = HomeRoute.Home.Route) {
        HomeView()
    }
}