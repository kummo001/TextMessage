package com.minhnha.textmessage.core.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.minhnha.textmessage.utils.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    fun provideContext(activity: ComponentActivity): ContextProvider {
        return object : ContextProvider {
            override fun getContext(): Context {
                return activity.applicationContext
            }
        }
    }
}