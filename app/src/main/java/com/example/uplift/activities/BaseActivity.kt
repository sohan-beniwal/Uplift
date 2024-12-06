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
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navigationView: NavigationView
    protected lateinit var toolbar: androidx.appcompat.widget.Toolbar
    protected lateinit var menuIcon: View
    protected lateinit var optionsIcon: View

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        // Initialize common views (ensure all activities include these IDs)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.app_bar)
        menuIcon = findViewById(R.id.menu_icon)
        optionsIcon = findViewById(R.id.options_icon)

        // Ensure content extends behind the status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Set status bar color to match the app bar
        window.statusBarColor = getColor(R.color.app_bar_color)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Handle menu icon clicks
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        // Handle options icon clicks
        optionsIcon.setOnClickListener {
            showOptionsMenu(it)
        }

        // Handle navigation drawer item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem)
            drawerLayout.closeDrawer(navigationView)
            true
        }

        // Handle drawer open/close status bar color changes
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
            R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
            R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_report -> startActivity(Intent(this, ReportActivity::class.java))
            R.id.nav_contactus -> startActivity(Intent(this, ContactUsActivity::class.java))
        }
    }

    private fun showOptionsMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.help -> {
                    Toast.makeText(this, "Help Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.about -> {
                    Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.logout -> {
                    Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
