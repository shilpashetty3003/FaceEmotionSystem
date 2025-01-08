package com.example.fermusicsystem.data.db

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [EmotionHistory::class], version = 1)
abstract class AppDatabase  :RoomDatabase(){
    abstract fun emotionHistoryDao(): EmotionHistoryDao

}