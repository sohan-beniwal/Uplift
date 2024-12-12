package com.example.uplift.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.uplift.R
import com.example.uplift.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val name = findViewById<TextView>(R.id.profile_name)
        val email = findViewById<TextView>(R.id.profile_email)
        val mobileNo = findViewById<TextView>(R.id.profile_mobileNo)
        val address = findViewById<TextView>(R.id.profile_address)

        val profileName = findViewById<TextView>(R.id.profile_name)
        val profileNameEdit = findViewById<EditText>(R.id.profile_name_editText)
        val editIconName = findViewById<ImageView>(R.id.profile_editIcon_name)

        val profileAddress = findViewById<TextView>(R.id.profile_address)
        val profileAddressEdit = findViewById<EditText>(R.id.profile_address_editText)
        val editIconAddress = findViewById<ImageView>(R.id.profile_editIcon_address)

        val saveButton = findViewById<View>(R.id.profile_btn_save)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(userId!!).child("userdata")

        // Set the email immediately, as it's already available from the current user object
        email.text = currentUser.email

        // Fetch user data from Firebase
        userRef.get()
            .addOnSuccessListener { snapshot ->
                // Convert the snapshot to a User object
                val user = snapshot.getValue(User::class.java)

                // Check if the user object is not null and update the UI
                if (user != null) {
                    name.text = user.name
                    mobileNo.text = user.mobile
                    address.text = user.address
                } else {
                    Toast.makeText(this, "User data is null", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                // Handle any errors while fetching data
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }

        editIconName.setOnClickListener {
            profileName.visibility = View.INVISIBLE // Hide the TextView
            profileNameEdit.visibility = View.VISIBLE // Show the EditText
            profileNameEdit.setText("") // Clear the EditText content
            profileNameEdit.requestFocus() // Focus on the EditText to show the blinking cursor
        }

        editIconAddress.setOnClickListener {
            profileAddress.visibility = View.INVISIBLE // Hide the TextView
            profileAddressEdit.visibility = View.VISIBLE // Show the EditText
            profileAddressEdit.setText("") // Clear the EditText content
            profileAddressEdit.requestFocus() // Focus on the EditText to show the blinking cursor
        }

        saveButton.setOnClickListener {
            val newName = profileNameEdit.text.toString().trim()
            if (newName.isNotEmpty()) {
                // Update Firebase Database
                userRef.child("name").setValue(newName)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show()
                        profileName.text = newName
                        profileNameEdit.visibility = View.GONE
                        profileName.visibility = View.VISIBLE
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish current Activity if it should not stay in the stack
    }
}
