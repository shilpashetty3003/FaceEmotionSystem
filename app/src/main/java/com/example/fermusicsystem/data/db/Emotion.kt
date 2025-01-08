package com.example.fermusicsystem.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "emotion_history")
data class EmotionHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val emotion:String,
    val timeStamp:Long)
