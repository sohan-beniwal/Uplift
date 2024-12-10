package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uplift.R
import com.example.uplift.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddAddressActivity : AppCompatActivity() {

    private val TAG = "AddAddressActivity"  // Tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        val houseNoEditText = findViewById<EditText>(R.id.add_address_houseNo)
        val buildingAreaEditText = findViewById<EditText>(R.id.add_address_buildingArea)
        val otherInfoEditText = findViewById<EditText>(R.id.add_address_otherInfo)
        val cityEditText = findViewById<EditText>(R.id.add_address_city)
        val stateEditText = findViewById<EditText>(R.id.add_address_state)
        val pincodeEditText = findViewById<EditText>(R.id.add_address_pincode)

        val signUp_button = findViewById<Button>(R.id.btn_add_address_signup)
        val text_view_login_1 = findViewById<TextView>(R.id.text_add_address_already_acc)
        val text_view_login_2 = findViewById<TextView>(R.id.text_add_address_login)

        fun validateInput(houseNo: String, buildingArea: String, otherinfo: String, city: String, state: String, pincode: String): Boolean {
            Log.d(TAG, "Validating input: houseNo=$houseNo, buildingArea=$buildingArea, otherinfo=$otherinfo, city=$city, state=$state, pincode=$pincode")

            if (houseNo.isEmpty() || buildingArea.isEmpty() || city.isEmpty() || state.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "All required fields are mandatory to be filled!", Toast.LENGTH_SHORT).show()
                return false
            }

            if (pincode.length != 6) {
                Toast.makeText(this, "Enter a valid Pincode!", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }

        signUp_button.setOnClickListener {
            val houseNo = houseNoEditText.text.toString().trim()
            val buildingArea = buildingAreaEditText.text.toString().trim()
            val otherInfo = otherInfoEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val state = stateEditText.text.toString().trim()
            val pincode = pincodeEditText.text.toString().trim()

            Log.d(TAG, "Button clicked. Values: houseNo=$houseNo, buildingArea=$buildingArea, otherInfo=$otherInfo, city=$city, state=$state, pincode=$pincode")

            if (validateInput(houseNo, buildingArea, otherInfo, city, state, pincode)) {
                val name = intent.getStringExtra("NAME")
                val email = intent.getStringExtra("EMAIL")
                val password = intent.getStringExtra("PASSWORD")
                val mobileNo = intent.getStringExtra("MOBILE_NO")
                val address = "$houseNo $buildingArea $otherInfo $city $state $pincode"
                val user = User(
                    name,mobileNo,city,address
                )
                Log.d(TAG, "Creating user with email: $email")

                auth.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            currentUser?.let {
                                // User creation successful
                                val userId = it.uid
                                Log.d(TAG, "User created successfully. User ID: $userId")

                                // Store the user data (address) in Firebase Realtime Database
                                val addressRef = database.child("users").child(userId).child("userdata")
                                addressRef.setValue(user)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Address stored successfully.")
                                        // Send verification email
                                        currentUser.sendEmailVerification()
                                            .addOnCompleteListener { emailTask ->
                                                if (emailTask.isSuccessful) {
                                                    Log.d(TAG, "Verification email sent successfully.")
                                                    Toast.makeText(this, "Account created! Please verify your email.", Toast.LENGTH_SHORT).show()

                                                    // Redirect to login page
                                                    val intent = Intent(this, login_activity::class.java)
                                                    startActivity(intent)
                                                } else {
                                                    Log.d(TAG, "Failed to send verification email.")
                                                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    }
                                    .addOnFailureListener {
                                        Log.e(TAG, "Failed to store address.", it)
                                        Toast.makeText(this, "Failed to store address.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Log.e(TAG, "User creation failed: ${task.exception?.message}")
                            Toast.makeText(this, "User creation failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        text_view_login_1.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
        text_view_login_2.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }
}
