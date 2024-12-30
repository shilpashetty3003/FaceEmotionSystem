package com.example.fermusicsystem.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class FaceBoundingBoxView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var boundingBox: RectF? = null
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }

    fun boundingBox(rect: Rect, imageWidth: Int, imageHeight: Int) {

        val viewWidth = width
        val viewHeight = height
        val scaleFactorX = viewWidth.toFloat() / imageHeight.toFloat()
        val scaleFactorY = viewHeight.toFloat() / imageWidth.toFloat()
        boundingBox = RectF(
            rect.left * scaleFactorX,
            rect.top * scaleFactorY,
            rect.right * scaleFactorX,
            rect.bottom * scaleFactorY
        )
        invalidate()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        boundingBox?.let {
            canvas?.drawRect(it, paint)
        }
    }



}