package com.example.fermusicsystem

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fermusicsystem.databinding.ActivityFaceDetectorBinding
//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.common.FirebaseVisionImage
//import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor

class FaceDetectorActivity : AppCompatActivity(), FrameProcessor {

    var cameraFacing: Facing = Facing.FRONT
    lateinit var binding: ActivityFaceDetectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.content.faceDetectionCameraView.facing = cameraFacing
        binding.content.faceDetectionCameraView.setLifecycleOwner(this)
        binding.content.faceDetectionCameraView.addFrameProcessor(this)
        binding.content.faceDetectionCameraToggleButton.setOnClickListener {
            cameraFacing = if (cameraFacing == Facing.FRONT) Facing.BACK else Facing.FRONT
            binding.content.faceDetectionCameraView.facing = cameraFacing
        }
    }

    override fun process(frame: Frame) {

//        val width = frame.size.width
//        val height = frame.size.height
//        val meta = FirebaseVisionImageMetadata.Builder()
//            .setWidth(width)
//            .setHeight(height)
//            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
//            .setRotation(if (cameraFacing == Facing.FRONT) FirebaseVisionImageMetadata.ROTATION_270 else FirebaseVisionImageMetadata.ROTATION_90)
//            .build()
//
//        var firebaseVisionImage = FirebaseVisionImage.fromByteArray(frame.getData(), meta)
//        val options = FirebaseVisionFaceDetectorOptions.Builder()
//            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
//            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE).build()
//
//
//        val faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options)
//
//
//        faceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener {
//            binding.content.faceDetectionCameraImageView.setImageBitmap(null)
//            val bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(bitmap)
//            var doPoint = Paint()
//            doPoint.color = Color.RED
//            doPoint.style = Paint.Style.FILL
//            doPoint.strokeWidth = 4F
//            var linePaint = Paint()
//            linePaint.color = Color.GREEN
//            linePaint.style = Paint.Style.STROKE
//            linePaint.strokeWidth = 2F
//            for (face in it) {
//                val faceContours = face.getContour(FirebaseVisionFaceContour.FACE).points
//                for ((i, contour) in faceContours.withIndex()) {
//                    if (i != faceContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            faceContours[i + 1].x,
//                            faceContours[i + 1].y,
//                            linePaint
//                        )
//                    else
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            faceContours[0].x,
//                            faceContours[0].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val leftEyebrowTopContours =
//                    face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).points
//                for ((i, contour) in leftEyebrowTopContours.withIndex()) {
//                    if (i != leftEyebrowTopContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            leftEyebrowTopContours[i + 1].x,
//                            leftEyebrowTopContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val leftEyebrowBottomContours =
//                    face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).points
//                for ((i, contour) in leftEyebrowBottomContours.withIndex()) {
//                    if (i != leftEyebrowBottomContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            leftEyebrowBottomContours[i + 1].x,
//                            leftEyebrowBottomContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val rightEyebrowTopContours =
//                    face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).points
//                for ((i, contour) in rightEyebrowTopContours.withIndex()) {
//                    if (i != rightEyebrowTopContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            rightEyebrowTopContours[i + 1].x,
//                            rightEyebrowTopContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val rightEyebrowBottomContours =
//                    face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).points
//                for ((i, contour) in rightEyebrowBottomContours.withIndex()) {
//                    if (i != rightEyebrowBottomContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            rightEyebrowBottomContours[i + 1].x,
//                            rightEyebrowBottomContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val leftEyeContours = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points
//                for ((i, contour) in leftEyeContours.withIndex()) {
//                    if (i != leftEyeContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            leftEyeContours[i + 1].x,
//                            leftEyeContours[i + 1].y,
//                            linePaint
//                        )
//                    else
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            leftEyeContours[0].x,
//                            leftEyeContours[0].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val rightEyeContours = face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).points
//                for ((i, contour) in rightEyeContours.withIndex()) {
//                    if (i != rightEyeContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            rightEyeContours[i + 1].x,
//                            rightEyeContours[i + 1].y,
//                            linePaint
//                        )
//                    else
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            rightEyeContours[0].x,
//                            rightEyeContours[0].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val upperLipTopContours =
//                    face.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).points
//                for ((i, contour) in upperLipTopContours.withIndex()) {
//                    if (i != upperLipTopContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            upperLipTopContours[i + 1].x,
//                            upperLipTopContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val upperLipBottomContours =
//                    face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points
//                for ((i, contour) in upperLipBottomContours.withIndex()) {
//                    if (i != upperLipBottomContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            upperLipBottomContours[i + 1].x,
//                            upperLipBottomContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val lowerLipTopContours =
//                    face.getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).points
//                for ((i, contour) in lowerLipTopContours.withIndex()) {
//                    if (i != lowerLipTopContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            lowerLipTopContours[i + 1].x,
//                            lowerLipTopContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val lowerLipBottomContours =
//                    face.getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).points
//                for ((i, contour) in lowerLipBottomContours.withIndex()) {
//                    if (i != lowerLipBottomContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            lowerLipBottomContours[i + 1].x,
//                            lowerLipBottomContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val noseBridgeContours =
//                    face.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points
//                for ((i, contour) in noseBridgeContours.withIndex()) {
//                    if (i != noseBridgeContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            noseBridgeContours[i + 1].x,
//                            noseBridgeContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                val noseBottomContours =
//                    face.getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).points
//                for ((i, contour) in noseBottomContours.withIndex()) {
//                    if (i != noseBottomContours.lastIndex)
//                        canvas.drawLine(
//                            contour.x,
//                            contour.y,
//                            noseBottomContours[i + 1].x,
//                            noseBottomContours[i + 1].y,
//                            linePaint
//                        )
//                    canvas.drawCircle(contour.x, contour.y, 4F, doPoint)
//                }
//
//                if (cameraFacing == Facing.FRONT) {
//                    val matrix = Matrix()
//                    matrix.preScale(-1F, 1F)
//                    val flippedBitmap =
//                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//                    binding.content.faceDetectionCameraImageView.setImageBitmap(flippedBitmap)
//                } else {
//                    binding.content.faceDetectionCameraImageView.setImageBitmap(bitmap)
//                }
//            }
//
//
//        }
//            .addOnCanceledListener {
//
//            }

    }
}