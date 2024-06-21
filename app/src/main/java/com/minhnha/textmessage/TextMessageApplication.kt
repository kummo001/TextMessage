package com.minhnha.textmessage

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TextMessageApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: TextMessageApplication
        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}