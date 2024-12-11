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
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:info@uplift.com")  // Use your email address
            }

            try {
                val packageManager = packageManager
                val availableApps = packageManager.queryIntentActivities(emailIntent, 0)

                if (availableApps.isNotEmpty()) {
                    startActivity(emailIntent)
                } else {
                    Toast.makeText(this, "No email app found!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error opening email app: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set onClickListener for Phone Icon
        phoneIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)  // Use ACTION_CALL if you want to directly call, ACTION_DIAL will open the dialer
            intent.data = Uri.parse("tel:6376396023")
            startActivity(intent)
        }

    }
}