package com.example.fermusicsystem

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.fermusicsystem.databinding.ActivityLauncherBinding
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LauncherActivity : AppCompatActivity() {


    var imagePreview: Preview? = null
    private lateinit var binding: ActivityLauncherBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File
    private var cameraInfo: CameraInfo? = null
    private var cameraControl: CameraControl? = null

    private var imageAnalysis: ImageAnalysis? = null

    val imageCapture =
        ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        setContentView(binding.root)
        if (allPermissionGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
        }

        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        binding.cameraTorchButton.setOnClickListener {
            toggleTourch()
        }
    }


    private fun startCamera() {

        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        cameraProvideFuture.addListener({

            imagePreview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(binding.previewView.display.rotation)
            }.build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageAnalysis =
                ImageAnalysis.Builder().setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
            imageAnalysis?.setAnalyzer(cameraExecutor, LuminousAnalyzer())
            val cameraProvider = cameraProvideFuture.get()
            try {
                cameraProvider.unbindAll()
                var camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageCapture,
                    imagePreview,
                    imageAnalysis
                )
                cameraControl = camera.cameraControl
                cameraInfo = camera.cameraInfo
            } catch (e: Exception) {

            }

            binding.previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {

        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        Log.d(TAG, "takePhoto: photofile $photoFile")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), object :
            ImageCapture.OnImageCapturedCallback() {

            @OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                Log.d(TAG, "onCaptureSuccess:  ${image.image}")
                image.close()

            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        })

//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    Log.d(TAG, "onImageSaved: ")
//                    val saveUri = Uri.fromFile(photoFile)
//                    val msg = "Photo capture $saveUri"
//                    Toast.makeText(this@LauncherActivity, msg, Toast.LENGTH_SHORT).show()
//
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Log.d(TAG, "onError:  $exception")
//                }
//
//            })
    }

    private fun getOutputDirectory(): File {

        Log.d(TAG, "getOutputDirectory:  ${externalMediaDirs.firstOrNull()}  filesDir $filesDir")
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            Log.d(TAG, "getOutputDirectory it:  ${it.absolutePath}")
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }


        Log.d(TAG, "getOutputDirectory: mkdir  $mediaDir   --- ${filesDir.absolutePath}")
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    class LuminousAnalyzer : ImageAnalysis.Analyzer {

        private var lastAnalysedTimeStamp = 0L;

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val currentTimeStamp = System.currentTimeMillis()
            if (currentTimeStamp - lastAnalysedTimeStamp >= TimeUnit.SECONDS.toMillis(1)) {
                val buffer = image.planes[0].buffer
                val data = buffer.toByteArray()
                val pixels = data.map { it.toInt() and 0xFF }

                val luma = pixels.average()
                Log.d(TAG, "analyze:luma  $luma")
                lastAnalysedTimeStamp = currentTimeStamp

            }

            image.close()

        }

    }


    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                startCamera()
            } else {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun toggleTourch() {
        if (cameraInfo?.torchState?.value == TorchState.ON) {
            cameraControl?.enableTorch(false)
        } else {
            cameraControl?.enableTorch(true)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val REQUEST_CODE_PERMISSION = 10

        private val REQUIRED_PERMISSION =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }

}