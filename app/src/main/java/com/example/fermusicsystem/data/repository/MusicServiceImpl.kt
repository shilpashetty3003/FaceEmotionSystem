package com.example.fermusicsystem.data.repository

import android.util.Log
import com.example.fermusicsystem.common.ResponseResult
import com.example.fermusicsystem.data.api.MusicAPI
import com.example.fermusicsystem.data.db.AppDatabase
import com.example.fermusicsystem.data.db.EmotionHistory
import com.example.fermusicsystem.data.model.Music

class MusicServiceImpl(private val musicApi: MusicAPI,private val appDatabase: AppDatabase) : MusicService {

    override suspend fun getMusicDetails(q: String): ResponseResult<Music> {
        return try {
            val response = musicApi.getMusicDetails(q)
            Log.d("TAG", "getMusicDetails: $response")

            if (response.isSuccessful) {
                response.body()?.let {
                    ResponseResult.Success(it)
                } ?: ResponseResult.Failure("Exception ${response.errorBody()}")
            } else {
                ResponseResult.Failure("Exception")
            }
        } catch (e: Exception) {
            ResponseResult.Failure("Exception ${e.localizedMessage}")
        }
    }

    override suspend fun addEmotionHistory(emotion: EmotionHistory) {
        appDatabase.emotionHistoryDao().insertEmotionHistory(emotionHistory = emotion)
    }

    override suspend fun getEmotionHistory(emotion: String): List<EmotionHistory> {
       return appDatabase.emotionHistoryDao().getEmotionHistoryByEmotion(emotion)
    }
}