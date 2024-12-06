package com.example.uplift.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.uplift.dataclass.Donation
import com.example.uplift.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DonateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

        // Reference the Spinners
        val spinner: Spinner = findViewById(R.id.mySpinner)
        val time: Spinner = findViewById(R.id.timespinner)
        val day: Spinner = findViewById(R.id.datespinner)
        val submitbtn : Button = findViewById(R.id.btn_submit)
        val itemname : EditText = findViewById(R.id.itemname)
        val quantity : EditText = findViewById(R.id.quantity)
        val additional : EditText = findViewById(R.id.additionalinformation)


        val items = resources.getStringArray(R.array.dropdown_items).toMutableList()
        val dayArr = listOf("Select Day", "Today", "Tomorrow")
        val timeArr = listOf("09:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00").toMutableList()

        items.add(0, "Select Category") // Add hint dynamically

        // Create an ArrayAdapter for the category spinner
        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item, // Custom layout with blue text
            items
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Disable the first item (hint)
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val selectedCategory = intent.getStringExtra("SELECTED_CATEGORY")
        if (selectedCategory != null) {
            preSelectCategory(spinner, items, selectedCategory)
        }

        // Adapter for the day spinner
        val adapterDay = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            dayArr
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Disable the first item (hint)
            }
        }
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        day.adapter = adapterDay

        // Function to filter valid times for "Today"
        fun getValidTimes(): List<String> {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            return timeArr.filter { time ->
                time >= currentTime
            }
        }

        // Adapter for the time spinner
        val adapterTime = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item,
            mutableListOf("Select Time") // Placeholder, will be updated dynamically
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Disable the first item (hint)
            }
        }
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        time.adapter = adapterTime

        // Listener for the day spinner
        day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDay = dayArr[position]
                val updatedTimes = when (selectedDay) {
                    "Today" -> listOf("Select Time") + getValidTimes() // Filter based on current time
                    "Tomorrow" -> listOf("Select Time") + timeArr      // All times are valid
                    else -> listOf("Select Time")                     // Reset to default
                }
                // Update the time spinner dynamically
                adapterTime.clear()
                adapterTime.addAll(updatedTimes)
                adapterTime.notifyDataSetChanged()
                time.setSelection(0) // Reset selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        submitbtn.setOnClickListener {
            val selectedCategory = spinner.selectedItem as String
            val selectedDay = day.selectedItem as String
            val selectedTime = time.selectedItem as String
            val itemnamefinal = itemname.text.toString()
            val quantityfinal = quantity.text.toString()
            val additionalfinal = additional.text.toString()


            if (selectedCategory == "Select Category" ||
                selectedDay == "Select Day" ||
                selectedTime == "Select Time" ||
                itemnamefinal.isEmpty() ||
                quantityfinal.isEmpty() ||
                !quantityfinal.isDigitsOnly() || // Checks if quantity contains only digits
                !itemnamefinal.all { it.isLetter() } // Checks if item name contains only letters
            ) {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create an object of the data class
            val donation = Donation(
                category = selectedCategory,
                item = itemnamefinal,
                quantity = quantityfinal,
                info = additionalfinal,
                day = selectedDay,
                time = selectedTime
            )

            // Pass the object to the database layer
            saveDonationToDatabase(donation)
        }
    }
    private fun saveDonationToDatabase(donation: Donation) {
        // Replace with your database logic
        // For example: Firebase Realtime Database or Room
        Toast.makeText(this, "Donation Saved: $donation", Toast.LENGTH_SHORT).show()
    }
    private fun preSelectCategory(spinner: Spinner, items: List<String>, category: String) {
        val position = items.indexOf(category)
        if (position != -1) {
            spinner.setSelection(position)
        }
    }
}
