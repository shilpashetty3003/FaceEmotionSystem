package com.example.fermusicsystem.screen.camera

import android.graphics.Bitmap
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fermusicsystem.common.FERModel
import husaynhakeem.io.facedetector.FaceBounds

class CameraViewModel : ViewModel() {


    private val emotionLabels = MutableLiveData<Map<Int, String>>()
    fun emotionLables(): LiveData<Map<Int, String>> = emotionLabels

    private var processing: Boolean = false

    fun onFaceDetected(faceBounds: List<FaceBounds>, faceBitmaps: List<Bitmap>) {
        if (faceBitmaps.isEmpty()) return

        synchronized(this) {

            if (!processing) {
                processing = true
                android.os.Handler(Looper.getMainLooper()).post {
                    emotionLabels.value = faceBounds.mapNotNull { it.id }
                        .zip(faceBitmaps)
                        .toMap().run { getEmotionsMap(this) }

                    processing = false

                }
            }
        }
    }

    fun getEmotionsMap(faceImages: Map<Int, Bitmap>): Map<Int, String> {
        val emotionLables = faceImages.map { FERModel.classify(it.value) }
        return faceImages.keys.zip(emotionLables).toMap()
    }


}