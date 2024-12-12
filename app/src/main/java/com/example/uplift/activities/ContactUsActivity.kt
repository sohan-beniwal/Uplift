package com.example.uplift.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.uplift.R

class ContactUsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        val emailIcon = findViewById<ImageView>(R.id.gmail_img)
        val phoneIcon = findViewById<ImageView>(R.id.phone_img)
        val emailText = findViewById<TextView>(R.id.gmail_text)
        val phoneText = findViewById<TextView>(R.id.phone_text)



        // Set onClickListener for Email Icon
        emailIcon.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822" // Email-specific MIME type
                putExtra(Intent.EXTRA_EMAIL, arrayOf("info@uplift.com")) // Replace with your email
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                putExtra(Intent.EXTRA_TEXT, "Write your query here...")
                setPackage("com.google.android.gm") // Restrict to Gmail
            }
            startActivity(emailIntent)
        }

        emailText.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822" // Email-specific MIME type
                putExtra(Intent.EXTRA_EMAIL, arrayOf("info@uplift.com")) // Replace with your email
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                putExtra(Intent.EXTRA_TEXT, "Write your query here...")
                setPackage("com.google.android.gm") // Restrict to Gmail
            }
            startActivity(emailIntent)
        }

        // Set onClickListener for Phone Icon
        phoneIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)  // Use ACTION_CALL if you want to directly call, ACTION_DIAL will open the dialer
            intent.data = Uri.parse("tel:6376396023")
            startActivity(intent)
        }

        phoneText.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)  // Use ACTION_CALL if you want to directly call, ACTION_DIAL will open the dialer
            intent.data = Uri.parse("tel:6376396023")
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish current Activity if it should not stay in the stack
    }

}