package com.example.uplift

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class MainActivity : AppCompatActivity() {

    private lateinit var imageSwitcher: ImageSwitcher
    private var currentIndex = 0

    // Arrays for images
    private val imagesArray = arrayOf(
        R.drawable.heading_1,
        R.drawable.heading_2,
        R.drawable.heading_3
    )

    // Handler for scheduling switching
    private val handler = Handler(Looper.getMainLooper())
    private val switchRunnable = object : Runnable {
        override fun run() {
            // Increment the index and reset if it exceeds the bounds
            currentIndex = (currentIndex + 1) % imagesArray.size

            // Set the next image with rounded corners
            setImageWithRoundedCorners(imagesArray[currentIndex])

            // Schedule the next switch
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Set the initial image with rounded corners
        setImageWithRoundedCorners(imagesArray[currentIndex])

        // Start switching automatically
        handler.postDelayed(switchRunnable, 3000)
    }

    private fun setImageWithRoundedCorners(imageResId: Int) {
        // Load the image as a Bitmap
        val bitmap = BitmapFactory.decodeResource(resources, imageResId)

        // Create a RoundedBitmapDrawable
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmapDrawable.isCircular = false  // Set to false to get rounded corners, not a circle
        roundedBitmapDrawable.cornerRadius = 300f  // Set corner radius

        // Set the image with rounded corners to ImageSwitcher
        imageSwitcher.setImageDrawable(roundedBitmapDrawable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(switchRunnable)
    }
}
