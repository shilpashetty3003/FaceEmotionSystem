package com.example.fermusicsystem.screen.musicsuggestion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fermusicsystem.common.ResponseResult
import com.example.fermusicsystem.data.db.EmotionHistory
import com.example.fermusicsystem.data.model.Music
import com.example.fermusicsystem.data.repository.MusicService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicSuggestionViewModel @Inject constructor(val musicService: MusicService) : ViewModel() {

    var _musicData = MutableLiveData<ResponseResult<Music>>()
    val musicData: LiveData<ResponseResult<Music>> = _musicData


    fun getVideoDetails(q: String) {
        Log.d("TAG", "getVideoDetails:  $q")

        viewModelScope.launch {
            _musicData.value = musicService.getMusicDetails(q)
        }
    }
    fun addEmotionToLocal(emotion:String){
        viewModelScope.launch{

            val emotionHistory = EmotionHistory(emotion = emotion, timeStamp = System.currentTimeMillis().toLong(), id = 0)
            musicService.addEmotionHistory(emotionHistory)
        }
    }


}