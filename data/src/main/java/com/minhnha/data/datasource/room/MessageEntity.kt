package com.minhnha.data.datasource.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var message: String,
    var endPointId: String
)