package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uplift.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is logged in, redirect to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not logged in, redirect to LoginActivity
            startActivity(Intent(this, login_activity::class.java))
        }

        // Finish SplashActivity to prevent it from staying in the back stack
        finish()

        setContentView(R.layout.activity_start)


        val login_button = findViewById<Button>(R.id.btn_start_login)
        val signUp_button = findViewById<Button>(R.id.btn_start_signup)
        val registerNgo = findViewById<TextView>(R.id.start_registerNGO)

        login_button.setOnClickListener{

            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
        signUp_button.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
        registerNgo.setOnClickListener{
            Log.d("ishika", "idscnsdnd")
            val intent = Intent(this, RegisterNgoActivity::class.java)
            startActivity(intent)
        }
    }
}