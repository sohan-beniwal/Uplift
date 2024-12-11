package com.example.uplift.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uplift.R
import com.example.uplift.dataclass.Donation

class DonationAdapter(private val donationList: List<Donation>) :
    RecyclerView.Adapter<DonationAdapter.UserViewHolder>() {

    // ViewHolder class
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var latitude: Double? = null
        private var longitude: Double? = null
        private var phonenumber : String? = null

        fun bind(donate: Donation) {
            // Set the category
            itemView.findViewById<TextView>(R.id.list_category).text = donate.category

            // Set the name
            itemView.findViewById<TextView>(R.id.list_name).text = donate.userName

            // Set the mobile number
            itemView.findViewById<TextView>(R.id.list_status).text = donate.userPhoneNumber
            longitude=donate.longitude
            latitude=donate.latitude
            phonenumber=donate.userPhoneNumber

            // Set the day and time
            itemView.findViewById<TextView>(R.id.list_day).text = donate.day
            itemView.findViewById<TextView>(R.id.list_time).text = donate.time
            // Handle options menu click
            itemView.findViewById<ImageView>(R.id.options_icon).setOnClickListener {
                // Add logic here for options menu click
            }
            itemView.findViewById<ImageView>(R.id.list_call).setOnClickListener {
                if (phonenumber != null && phonenumber!!.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)  // Use ACTION_CALL if you want to directly call, ACTION_DIAL will open the dialer
                    intent.data = Uri.parse("tel:$phonenumber")
                    itemView.context.startActivity(intent)
                }
            }
            itemView.findViewById<ImageView>(R.id.list_directions).setOnClickListener {
                if (latitude != null && longitude != null) {
                    // Create the URL for Google Maps with directions
                    val geoUri = "google.navigation:q=$latitude,$longitude&mode=d"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                    intent.setPackage("com.google.android.apps.maps")  // This ensures that Google Maps opens

                    // Check if there is an app that can handle this intent
                    if (intent.resolveActivity(itemView.context.packageManager) != null) {
                        itemView.context.startActivity(intent)
                    } else {
                        // Handle the case where Google Maps is not installed (optional)
                        Log.e("Maps", "Google Maps is not installed.")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(donationList[position])
    }

    override fun getItemCount() = donationList.size
}
