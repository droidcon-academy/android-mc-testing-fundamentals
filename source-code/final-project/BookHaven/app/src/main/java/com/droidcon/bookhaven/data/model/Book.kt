package com.droidcon.bookhaven.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val author: String,
    val year: String,
    val notes: String,
    val timestamp: Long,
)
