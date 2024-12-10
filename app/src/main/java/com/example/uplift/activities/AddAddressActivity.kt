package com.example.uplift.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uplift.R
import com.example.uplift.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class AddAddressActivity : AppCompatActivity() {

    private val TAG = "AddAddressActivity"

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
        val alreadyacc = findViewById<TextView>(R.id.text_add_address_already_acc)

        val signUpButton = findViewById<Button>(R.id.btn_add_address_signup)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Validating address...")

        suspend fun validateInput(
            houseNo: String,
            buildingArea: String,
            otherinfo: String,
            city: String,
            state: String,
            pincode: String
        ): Boolean {
            if (houseNo.isEmpty() || buildingArea.isEmpty() || city.isEmpty() || state.isEmpty() || pincode.isEmpty()) {
                Log.e(TAG, "All required fields must be filled!")
                return false
            }

            if (pincode.length != 6) {
                Log.e(TAG, "Invalid Pincode length!")
                return false
            }

            val address = "$houseNo, $buildingArea, $otherinfo, $city, $state, $pincode"
            val apiKey = "AIzaSyCp9qkv1uNtVJXFYPvQHb2-ATWYUpyPLJQ"
            val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$apiKey"

            return withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()

                    response.body?.let { responseBody ->
                        val json = responseBody.string()
                        val jsonObject = JSONObject(json)
                        val status = jsonObject.getString("status")

                        return@withContext (status == "OK")
                    } ?: false
                } catch (e: Exception) {
                    Log.e(TAG, "Error during address validation: $e")
                    return@withContext false
                }
            }
        }

        signUpButton.setOnClickListener {
            val houseNo = houseNoEditText.text.toString().trim()
            val buildingArea = buildingAreaEditText.text.toString().trim()
            val otherInfo = otherInfoEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val state = stateEditText.text.toString().trim()
            val pincode = pincodeEditText.text.toString().trim()

            lifecycleScope.launch {
                progressDialog.show()

                val isValid = validateInput(houseNo, buildingArea, otherInfo, city, state, pincode)
                progressDialog.dismiss()

                if (isValid) {
                    val name = intent.getStringExtra("NAME") ?: "Unknown"
                    val email = intent.getStringExtra("EMAIL") ?: ""
                    val password = intent.getStringExtra("PASSWORD") ?: ""
                    val mobileNo = intent.getStringExtra("MOBILE_NO") ?: ""
                    val address = "$houseNo $buildingArea $otherInfo $city $state $pincode"
                    val user = User(name, mobileNo, city, address)

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val currentUser = auth.currentUser
                                currentUser?.let {
                                    val userId = it.uid
                                    database.child("users").child(userId).child("userdata").setValue(user)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "Address stored successfully.")
                                            currentUser.sendEmailVerification()
                                                .addOnCompleteListener { emailTask ->
                                                    if (emailTask.isSuccessful) {
                                                        Toast.makeText(this@AddAddressActivity, "Account created! Please verify your email.", Toast.LENGTH_SHORT).show()
                                                        startActivity(Intent(this@AddAddressActivity, login_activity::class.java))
                                                    } else {
                                                        Toast.makeText(this@AddAddressActivity, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e(TAG, "Failed to store address.", exception)
                                            Toast.makeText(this@AddAddressActivity, "Failed to store address.", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Log.e(TAG, "User creation failed: ${task.exception?.message}")
                                Toast.makeText(this@AddAddressActivity, "User creation failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@AddAddressActivity, "Invalid address. Please check and try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        alreadyacc.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }
}