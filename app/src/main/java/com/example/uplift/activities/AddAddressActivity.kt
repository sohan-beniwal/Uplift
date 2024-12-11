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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder

class AddAddressActivity : AppCompatActivity() {

    private val TAG = "GeoCodingAPI"
    private lateinit var progressDialog: ProgressDialog

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

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Validating address...")

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
                    progressDialog.setMessage("Creating Account")
                    progressDialog.show()
                    val name = intent.getStringExtra("NAME") ?: "Unknown"
                    val email = intent.getStringExtra("EMAIL") ?: ""
                    val password = intent.getStringExtra("PASSWORD") ?: ""
                    val mobileNo = intent.getStringExtra("MOBILE_NO") ?: ""
                    val address = "$houseNo $buildingArea $otherInfo $city $state $pincode"

                    val (latitude, longitude) = fetchCoordinates(address)

                    if (latitude != 0.0 && longitude != 0.0) {
                        val user = User(name, mobileNo, city, address, admin = false, longitude, latitude)

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUser = auth.currentUser
                                    currentUser?.let {
                                        val userId = it.uid
                                        database.child("users").child(userId).child("userdata")
                                            .setValue(user)
                                            .addOnSuccessListener {
                                                Log.d(TAG, "Address stored successfully.")
                                                currentUser.sendEmailVerification()
                                                    .addOnCompleteListener { emailTask ->
                                                        if (emailTask.isSuccessful) {
                                                            Toast.makeText(
                                                                this@AddAddressActivity,
                                                                "Account created! Please verify your email.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            startActivity(
                                                                Intent(this@AddAddressActivity, login_activity::class.java)
                                                            )
                                                        } else {
                                                            Toast.makeText(
                                                                this@AddAddressActivity,
                                                                "Failed to send verification email.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e(TAG, "Failed to store address.", exception)
                                                Toast.makeText(
                                                    this@AddAddressActivity,
                                                    "Failed to store address.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                } else {
                                    Log.e(TAG, "User creation failed: ${task.exception?.message}")
                                    Toast.makeText(
                                        this@AddAddressActivity,
                                        "User creation failed: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            this@AddAddressActivity,
                            "Invalid address. Please check and try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressDialog.dismiss()
                } else {
                    Toast.makeText(
                        this@AddAddressActivity,
                        "Invalid address. Please check and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        alreadyacc.setOnClickListener {
            val intent = Intent(this, login_activity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun validateInput(
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
        val apiKey = getString(R.string.google_maps_api_key)  // Load API key from strings.xml
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${URLEncoder.encode(address, "UTF-8")}&key=$apiKey"

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

    private suspend fun fetchCoordinates(address: String): Pair<Double, Double> {
        val apiKey = getString(R.string.google_maps_api_key)  // Load API key from strings.xml
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${URLEncoder.encode(address, "UTF-8")}&key=$apiKey"

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val results = jsonResponse.getJSONArray("results")

                    if (results.length() > 0) {
                        val geometry = results.getJSONObject(0).getJSONObject("geometry")
                        val location = geometry.getJSONObject("location")
                        val latitude = location.getDouble("lat")
                        val longitude = location.getDouble("lng")

                        Pair(latitude, longitude)  // Return latitude and longitude as a Pair
                    } else {
                        Pair(0.0, 0.0)  // Default value in case of no results
                    }
                } else {
                    Pair(0.0, 0.0)  // Return default value in case of failure
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Pair(0.0, 0.0)  // Return default value in case of an error
            }
        }
    }
}
