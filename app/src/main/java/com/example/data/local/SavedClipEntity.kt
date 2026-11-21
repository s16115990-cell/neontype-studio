package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_clips")
data class SavedClipEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val category: String,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
