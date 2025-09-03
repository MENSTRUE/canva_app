package com.android.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // Path untuk coretan user
    private var path = Path()

    // Paint untuk coretan user
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 92f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    // Paint untuk garis panduan/grid (putus-putus)
    private val guidePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 4f
        style = Paint.Style.STROKE
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0f)
    }

    // Bitmap yang ingin ditampilkan (misal: R.drawable.squareroot)
    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.squareroot)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val margin = 100f

        // Garis vertikal tengah (putus-putus)
        canvas.drawLine(w / 2, margin, w / 2, h - margin, guidePaint)

        // Garis horizontal tengah (putus-putus)
        canvas.drawLine(margin, h / 2, w - margin, h / 2, guidePaint)

        // Gambar bitmap di tengah canvas
        val centerX = (width - bitmap.width) / 2f
        val centerY = (height - bitmap.height) / 2f
        canvas.drawBitmap(bitmap, centerX, centerY, paint)

        // Gambar coretan user
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
            }
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        path.reset()
        invalidate()
    }
}
