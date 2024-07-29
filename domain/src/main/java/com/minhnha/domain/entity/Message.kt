package com.minhnha.domain.entity

import com.google.android.gms.common.internal.Objects

data class Message(
    val id: Int? = null,
    val message: String,
    val endPointId: String
)