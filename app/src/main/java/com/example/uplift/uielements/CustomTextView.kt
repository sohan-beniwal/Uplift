package com.example.uplift.uielements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.uplift.R

class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private val strokePaint = Paint()
    private val fillPaint = Paint()

    init {
        // Stroke paint settings (outline)
        strokePaint.isAntiAlias = true
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = ContextCompat.getColor(context, R.color.app_bar_color)  // Stroke color (outline)
        strokePaint.strokeWidth = 12f   // Stroke width

        // Fill paint settings
        fillPaint.isAntiAlias = true
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = ContextCompat.getColor(context, R.color.app_bar_text)  // Fill color (inside the text)
    }

    override fun onDraw(canvas: Canvas) {
        // Calculate the x and y positions to center the text properly
        val xPos = (width - paint.measureText(text.toString())) / 2
        val yPos = (height / 2) - ((paint.descent() + paint.ascent()) / 2)

        // Draw the stroke text first (outline)
        strokePaint.textSize = textSize
        canvas.drawText(text.toString(), xPos, yPos, strokePaint)

        // Draw the fill text on top of the stroke
        fillPaint.textSize = textSize
        canvas.drawText(text.toString(), xPos, yPos, fillPaint)

        super.onDraw(canvas)
    }
}
