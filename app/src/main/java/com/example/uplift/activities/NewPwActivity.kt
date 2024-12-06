package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uplift.R

class NewPwActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pw)


        val submit_button = findViewById<Button>(R.id.btn_new_pw_submit)


        submit_button.setOnClickListener{
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }
}