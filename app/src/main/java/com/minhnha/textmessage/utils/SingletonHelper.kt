package com.minhnha.textmessage.utils

class SingletonHelper private constructor() {
    companion object {

        @Volatile
        private var instance: SingletonHelper? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: SingletonHelper().also { instance = it }
            }
    }

    fun doSomething() = "Doing something"
}