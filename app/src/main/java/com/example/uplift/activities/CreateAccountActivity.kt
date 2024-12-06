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

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val next_button = findViewById<Button>(R.id.btn_create_acc_next)
        val text_view_login_1 = findViewById<TextView>(R.id.text_create_acc_2)
        val text_view_login_2 = findViewById<TextView>(R.id.text_create_acc_3)

        next_button.setOnClickListener{

            val intent = Intent(this, AddAddressActivity::class.java)
            startActivity(intent)
        }
        text_view_login_1.setOnClickListener{
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
        text_view_login_2.setOnClickListener{
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }
}