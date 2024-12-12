package com.example.uplift.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.uplift.R
import com.example.uplift.activities.BaseActivity
import com.example.uplift.activities.DonateActivity
import com.example.uplift.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : BaseActivity() {

    private lateinit var imageSwitcher: ImageSwitcher
    private var currentIndex = 0
    private lateinit var donateBtn: Button
    private lateinit var foodBtn: ImageView
    private lateinit var moneyBtn: ImageView
    private lateinit var otherBtn: ImageView
    private lateinit var clothBtn: ImageView

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

        // Initialize buttons and views
        //donateBtn = findViewById(R.id.btn_donate)
        clothBtn = findViewById(R.id.image2)
        foodBtn = findViewById(R.id.image1)
        otherBtn = findViewById(R.id.image4)
        moneyBtn = findViewById(R.id.image3)
        val username = findViewById<TextView>(R.id.username)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(userId!!).child("userdata")

        userRef.get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    username.text = "Welcome, ${user.name}\u2764\uFE0F"
                } else {
                    Toast.makeText(this, "User data is null", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }

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

        // Start switching images automatically
        handler.postDelayed(switchRunnable, 3000)

        // Set onClickListeners for donation buttons
        clothBtn.setOnClickListener { navigateToDonateScreen("Clothes") }
        foodBtn.setOnClickListener { navigateToDonateScreen("Food") }
        moneyBtn.setOnClickListener {
            val upiAddress = "soniishika201@okicici" // Replace with the actual UPI ID or the payee UPI address.
            val amount = "0.00" // Amount to be transferred
            val note = "Donation" // Optional note
            val transactionRefId = "Txn12345" // Unique transaction reference

            val uri = Uri.parse("upi://pay?pa=$upiAddress&pn=Donation&mc=0000&tid=$transactionRefId&tr=$transactionRefId&tn=$note&am=$amount&cu=INR")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.android.chrome"))
            startActivity(intent)
        }

        otherBtn.setOnClickListener { navigateToDonateScreen("Other") }
    }

    private fun setImageWithRoundedCorners(imageResId: Int) {
        // Load the image as a Bitmap
        val bitmap = BitmapFactory.decodeResource(resources, imageResId)

        // Create a RoundedBitmapDrawable
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmapDrawable.isCircular = false // Set to false to get rounded corners, not a circle
        roundedBitmapDrawable.cornerRadius = 300f // Set corner radius

        // Set the image with rounded corners to ImageSwitcher
        imageSwitcher.setImageDrawable(roundedBitmapDrawable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(switchRunnable)
    }

    private fun navigateToDonateScreen(category: String) {
        val intent = Intent(this, DonateActivity::class.java)
        intent.putExtra("SELECTED_CATEGORY", category)
        startActivity(intent)
    }
    override fun onBackPressed() {
        // Exit the app
        finishAffinity() // Closes all activities and exits the app
    }
}