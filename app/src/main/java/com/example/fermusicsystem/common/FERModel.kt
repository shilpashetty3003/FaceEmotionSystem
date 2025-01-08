package com.example.fermusicsystem.common

import android.content.Context
import android.graphics.Bitmap
import com.example.fermusicsystem.utils.BitmapUtils.toGrayscale
import com.example.fermusicsystem.utils.BitmapUtils.toGrayscaleByteBuffer
import com.google.mlkit.vision.common.InputImage
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp

object FERModel {

    private const val MODEL_FILE_NAME = "fer_model.tflite"
    private const val LABELS_FILE_NAME = "fer_model.names"

    private const val INPUT_IMAGE_WIDTH = 48
    private const val INPUT_IMAGE_HEIGHT = 48

    private const val N_CLASSES = 8


    private lateinit var interpreter: Interpreter
    private lateinit var labels: ArrayList<String>

    fun load(context: Context): Interpreter {
        if (!this::interpreter.isInitialized) {
            synchronized(FERModel::class.java) {
                interpreter = loadModelFromAssest(context)
                labels = loadLabelsFromAssets(context = context)
            }

        }
        return interpreter
    }


    private fun loadModelFromAssest(context: Context): Interpreter {
        val model = context.assets.open(MODEL_FILE_NAME).readBytes()
        val buffer = ByteBuffer.allocateDirect(model.size).order(ByteOrder.nativeOrder())
        buffer.put(model)
        return Interpreter(buffer)
    }

    private fun loadLabelsFromAssets(context: Context): ArrayList<String> {
        return ArrayList<String>().apply {
            context.assets.open(LABELS_FILE_NAME).bufferedReader().forEachLine { add(it) }
        }
    }

    fun classify(inputImage: Bitmap) :String{

        val input = Bitmap.createScaledBitmap(inputImage, INPUT_IMAGE_WIDTH, INPUT_IMAGE_HEIGHT,true).toGrayscale().toGrayscaleByteBuffer()
        return predict(input).toPrediction().toLabel()
    }
    fun predict(input:ByteBuffer) :FloatArray{
        val outputByteBuffer = ByteBuffer.allocateDirect(4 * N_CLASSES).order(ByteOrder.nativeOrder())

        interpreter.run(input,outputByteBuffer)

        val logits = FloatArray(N_CLASSES)
        val labelBuffer = outputByteBuffer.run {
            rewind()
            asFloatBuffer()
        }

        labelBuffer.get(logits)
        return logits

    }

    private fun FloatArray.toPrediction():Int{
        var probabilities = this.map { exp(it.toDouble()) }
            .run { map { it / sum() } }
        val test = probabilities.sum().toInt() == 1

        // Class with max probability
        return probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
    }

    private fun Int.toLabel() = labels[this]



}