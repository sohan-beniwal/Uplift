package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import com.example.uplift.R

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish current Activity if it should not stay in the stack
    }

}