package com.example.fermusicsystem.screen

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fermusicsystem.common.FERModel
import com.example.fermusicsystem.databinding.ActivityHomeScreenBinding
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.size.SizeSelectors
import husaynhakeem.io.facedetector.FaceBounds
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.Frame
import husaynhakeem.io.facedetector.LensFacing

class HomeScreen : AppCompatActivity() {


    private val viewModel = ViewModelProvider.NewInstanceFactory().create(HomeViewModel::class.java)

    lateinit var binding: ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lensFacing = Facing.FRONT
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
        val faceDetector =
            FaceDetector(faceBoundsOverlay = binding.faceBoundsOverlay).also { it.setup() }
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

    }

    fun setupObserver() {
        viewModel.emotionLables().observe(this){
            it?.let { binding.faceBoundsOverlay.updateEmotionLabels(it) }
        }
    }

    fun FaceDetector.setup() = run {
        setOnFaceDetectionListener(object : FaceDetector.OnFaceDetectionResultListener {
            override fun onSuccess(faceBounds: List<FaceBounds>, faceBitmaps: List<Bitmap>) {
                        viewModel.onFaceDetected(faceBounds,faceBitmaps)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }


}