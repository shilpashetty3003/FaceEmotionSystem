package com.example.fermusicsystem.screen.camera

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fermusicsystem.common.FERModel
import com.example.fermusicsystem.databinding.ActivityHomeScreenBinding
import com.example.fermusicsystem.screen.musicsuggestion.MusicSuggestScreen
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.size.SizeSelectors
import husaynhakeem.io.facedetector.FaceBounds
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.Frame
import husaynhakeem.io.facedetector.LensFacing


class CameraScreen : AppCompatActivity() {


    private val viewModel =
        ViewModelProvider.NewInstanceFactory().create(CameraViewModel::class.java)
    lateinit var binding: ActivityHomeScreenBinding
    lateinit var faceDetector: FaceDetector

    var lensFacing: Facing = Facing.FRONT
    var lastUpdateEmotion = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewfinder.setLifecycleOwner(this)
        binding.toggleCameraButton.setOnClickListener {
            toggleCamera()
        }

        faceDetector =
            FaceDetector(faceBoundsOverlay = binding.faceBoundsOverlay).also { it.setup() }

        setupCamera(lensFacing)
        FERModel.load(this)
        setupObserver()

    }

    override fun onStart() {
        super.onStart()
        binding.viewfinder.open()
    }

    override fun onStop() {
        super.onStop()
        binding.viewfinder.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewfinder.destroy()

    }

    fun setupCamera(lensFacing: Facing) {

        binding.viewfinder.facing = lensFacing
        binding.viewfinder.audio = Audio.OFF
        binding.viewfinder.setPreviewStreamSize(SizeSelectors.maxWidth(480))

        binding.viewfinder.addFrameProcessor {
            faceDetector.process(
                Frame(
                    it.getData(),
                    rotation = it.rotation,
                    size = android.util.Size(it.size.width, it.size.height),
                    format = it.format,
                    lensFacing = LensFacing.FRONT
                )
            )
        }


        binding.viewfinder.addFrameProcessor { frame ->
            Log.d("TAG", "process: ${frame.format}")
            faceDetector?.process(
                Frame(
                    frame.getData(),
                    frame.rotation,
                    size = android.util.Size(frame.size.width, frame.size.height),
                    format = frame.format,
                    lensFacing = LensFacing.FRONT
                )
            )
        }

    }

    private fun setupObserver() {
        viewModel.emotionLables().observe(this) {
            lastUpdateEmotion = it.values.last()
            it?.let { binding.faceBoundsOverlay.updateEmotionLabels(it) }
        }
    }

    private fun FaceDetector.setup() = run {

        setOnFaceDetectionListener(object : FaceDetector.OnFaceDetectionResultListener {
            override fun onSuccess(faceBounds: List<FaceBounds>, faceBitmaps: List<Bitmap>) {
                viewModel.onFaceDetected(faceBounds, faceBitmaps)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    fun toggleCamera() {
        lensFacing = if (lensFacing == Facing.BACK) Facing.FRONT else Facing.BACK
        setupCamera(lensFacing)
    }

    fun takePhoto(view: View) {

        var intent = Intent(this, MusicSuggestScreen::class.java)

        intent.putExtra("data", lastUpdateEmotion)
        startActivity(intent)

    }


}


