package com.minhnha.domain.util

sealed class Result {
    data object Success : Result()
    data class Error(val message: String) : Result()
    data object Empty : Result()
}