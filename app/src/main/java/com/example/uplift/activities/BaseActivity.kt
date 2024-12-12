package com.example.uplift.activities

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.uplift.activities.MainActivity
import com.example.uplift.R
import com.example.uplift.dataclass.Donation
import com.example.uplift.dataclass.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

open class BaseActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected lateinit var toolbar: androidx.appcompat.widget.Toolbar
    protected lateinit var menuIcon: View
    protected lateinit var optionsIcon: View

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.app_bar)
        menuIcon = findViewById(R.id.menu_icon)
        optionsIcon = findViewById(R.id.options_icon)

        // Extend content behind the status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Set status bar color
        window.statusBarColor = getColor(R.color.app_bar_color)

        // Setup toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Handle menu icon click
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        // Handle options icon click
        optionsIcon.setOnClickListener {
            showOptionsMenu(it)
        }

        // Check user role and update menu items
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(userId!!).child("userdata")

        userRef.get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                val isAdmin = user?.admin ?: false
                val navMenu = navigationView.menu
                val navRequestItem = navMenu.findItem(R.id.nav_request)

                // Hide or show 'nav_request' based on the admin role
                navRequestItem.isVisible = isAdmin
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }


        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem)
            drawerLayout.closeDrawer(navigationView)
            true
        }

        // Update status bar color based on drawer state
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                window.statusBarColor = getColor(R.color.app_bar_text)
            }

            override fun onDrawerClosed(drawerView: View) {
                window.statusBarColor = getColor(R.color.app_bar_color)
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    private fun handleNavigationItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish() }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            R.id.nav_contactus -> {
                startActivity(Intent(this, ContactUsActivity::class.java))
                finish()
            }
            R.id.nav_request-> {
                startActivity(Intent(this, RequestActivity::class.java))
                finish()
            }
        }
    }

    private fun showOptionsMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.help -> {
                    val intent = Intent(this, ContactUsActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.about -> {
                    val intent = Intent(this, ContactUsActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, login_activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}

