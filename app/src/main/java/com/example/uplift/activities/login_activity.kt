package com.example.uplift.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uplift.R
import com.google.firebase.auth.FirebaseAuth

class login_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_button  = findViewById<Button>(R.id.btn_login_1)
        val text_view_forgot_pw = findViewById<TextView>(R.id.text_login_4)
        val text_view_signup = findViewById<TextView>(R.id.text_login_5)
        val email = findViewById<EditText>(R.id.login_screen_email)
        val password = findViewById<EditText>(R.id.login_screen_password)


        login_button.setOnClickListener {
            var progressDialog =  ProgressDialog(this)
            progressDialog.setMessage("Authenticating")
            progressDialog.show()

            val email = email.text.toString()  // Replace emailInput with your email field's ID
            val password = password.text.toString()  // Replace passwordInput with your password field's ID

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Authenticate the user
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                // Email is verified
                                val intent = Intent(this, MainActivity::class.java)
                                progressDialog.dismiss()
                                startActivity(intent)
                                finish() // Optional: Close the login activity
                            } else {
                                // Email is not verified
                                Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        // Authentication failed
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
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