package com.minhnha.textmessage.navigation

abstract class TextMessageRoute {
    abstract val route: String
}

sealed class HomeRoute : TextMessageRoute() {
    class Home : HomeRoute() {
        override val route = Route

        companion object {
            const val Route = "Home"
        }
    }
}

sealed class SplashRoute : TextMessageRoute() {
    class Splash : SplashRoute() {
        override val route = Route

        companion object {
            const val Route = "Splash"
        }
    }
}

sealed class MessageHistoryRoute : TextMessageRoute() {
    class MessageHistory : SplashRoute() {
        override val route = Route

        companion object {
            const val Route = "MessageHistory"
        }
    }
}