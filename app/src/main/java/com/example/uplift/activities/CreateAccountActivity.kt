package com.example.uplift.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uplift.R

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val nameEditText = findViewById<EditText>(R.id.create_acc_name)
        val emailEditText = findViewById<EditText>(R.id.create_acc_email)
        val passwordEditText = findViewById<EditText>(R.id.create_acc_password)
        val mobileEditText = findViewById<EditText>(R.id.create_acc_mobileNo)
        val nextButton = findViewById<Button>(R.id.btn_create_acc_next)

        val text_view_login_1 = findViewById<TextView>(R.id.text_create_acc_already_acc)

        fun validateInput(name: String, email: String, password: String, mobileNo: String): Boolean
        {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobileNo.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return false
            }

            if (password.length < 8) {
                Toast.makeText(this, "Password must be atleast 8 characters long.", Toast.LENGTH_SHORT).show()
                return false
            }

            if(!mobileNo.isDigitsOnly() || mobileNo.length != 10) {
                Toast.makeText(this, "Enter a valid mobile number.", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }


        nextButton.setOnClickListener{

            val name=nameEditText.text.toString().trim()
            val email=emailEditText.text.toString().trim()
            val password=passwordEditText.text.toString().trim()
            val mobileNo=mobileEditText.text.toString().trim()


            if(validateInput(name, email, password, mobileNo))
            {
                val intent = Intent(this, AddAddressActivity::class.java)
                intent.putExtra("NAME", name)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                intent.putExtra("MOBILE_NO", mobileNo)
                startActivity(intent)
            }

        }

        text_view_login_1.setOnClickListener{
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }
}