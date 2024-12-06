package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uplift.R

class login_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val login_button = findViewById<Button>(R.id.btn_login_1)
        val text_view_forgot_pw = findViewById<TextView>(R.id.text_login_4)
        val text_view_signup = findViewById<TextView>(R.id.text_login_5)

        login_button.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        text_view_forgot_pw.setOnClickListener{
            val intent = Intent(this, ForgotPwActivity::class.java)
            startActivity(intent)
        }
        text_view_signup.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}