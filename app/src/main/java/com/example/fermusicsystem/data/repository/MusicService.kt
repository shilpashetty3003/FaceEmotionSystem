package com.example.fermusicsystem.data.repository

import com.example.fermusicsystem.common.ResponseResult
import com.example.fermusicsystem.data.db.EmotionHistory
import com.example.fermusicsystem.data.model.Music
import retrofit2.Response

interface MusicService {

    suspend fun getMusicDetails(q:String) :  ResponseResult<Music>
    suspend fun addEmotionHistory(emotion: EmotionHistory)
    suspend fun getEmotionHistory(emotion:String): List<EmotionHistory>
}