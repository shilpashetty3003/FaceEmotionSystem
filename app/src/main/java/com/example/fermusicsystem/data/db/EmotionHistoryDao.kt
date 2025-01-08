package com.example.fermusicsystem.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface EmotionHistoryDao{

    @Insert
    suspend fun insertEmotionHistory(emotionHistory: EmotionHistory)

    // Get all emotion history records ordered by timestamp (latest first)
    @Query("SELECT * FROM emotion_history ORDER BY timestamp DESC")
    suspend fun getAllEmotionHistory(): List<EmotionHistory>

    // Get emotion history by specific emotion (e.g., "happy")
    @Query("SELECT * FROM emotion_history WHERE emotion = :emotion ORDER BY timestamp DESC")
    suspend fun getEmotionHistoryByEmotion(emotion: String): List<EmotionHistory>
}
