package com.project.mycare

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

class DocDashActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doc_dash)

        // Initialize DBHelper
        dbHelper = DBHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard"
        // Set User Profile Information
        val usernameTextView: TextView = findViewById(R.id.username)
        val userEmailTextView: TextView = findViewById(R.id.userEmail)
        val profileImageView: ImageView = findViewById(R.id.profileImage)

        // Fetch user data from database
        val currentUser = dbHelper.getCurrentUser()

        // Check if user data is retrieved successfully
        if (currentUser != null) {
            usernameTextView.text = currentUser.username
            userEmailTextView.text = currentUser.email
        } else {
            // Handle case where user data could not be retrieved
            usernameTextView.text = getString(R.string.unknown_dash_message)
            userEmailTextView.text = getString(R.string.no_email_dash_message)
            // Set a default profile image or handle absence of profile image
        }

        // Set Dashboard Items
        setupDashboardItems()
    }

    private fun setupDashboardItems() {
        // You can dynamically add items here if needed
    }
}
