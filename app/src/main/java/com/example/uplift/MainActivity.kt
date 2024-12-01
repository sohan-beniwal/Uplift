package com.example.uplift

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var imageSwitcher: ImageSwitcher
    private lateinit var textSwitcher: TextSwitcher
    private var currentIndex = 0

    // Arrays for names and images
    private val namesArray = arrayOf("Food", "Clothes", "Money", "Other")
    private val imagesArray = arrayOf(
        R.drawable.food,
        R.drawable.cloth,
        R.drawable.money,
        R.drawable.other
    )

    // Handler for scheduling switching
    private val handler = Handler(Looper.getMainLooper())
    private val switchRunnable = object : Runnable {
        override fun run() {
            // Update to the next item
            currentIndex = (currentIndex + 1) % namesArray.size

            // Set new text and image
            textSwitcher.setText(namesArray[currentIndex])
            imageSwitcher.setImageResource(imagesArray[currentIndex])

            // Schedule the next switch
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize TextSwitcher
        textSwitcher = findViewById(R.id.switch_text)
        textSwitcher.setFactory {
            TextView(this).apply {
                textSize = 40f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.app_bar_color))
            }
        }

        // Initialize ImageSwitcher
        imageSwitcher = findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory {
            ImageView(this).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }


        // Set initial text and image
        textSwitcher.setText(namesArray[currentIndex])
        imageSwitcher.setImageResource(imagesArray[currentIndex])

        // Start switching automatically
        handler.postDelayed(switchRunnable, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(switchRunnable)
    }
}
