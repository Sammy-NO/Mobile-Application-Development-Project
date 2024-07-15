package com.project.mycare

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class Dash : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_dash)

        // Initialize DBHelper
        dbHelper = DBHelper(this)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Setup Toolbar
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

    @SuppressLint("SuspiciousIndentation")
    private fun setupDashboardItems() {
        // Example: Uncomment and set up your dashboard items here
    val ChildServicesMenu: LinearLayout = findViewById(R.id.childservices)
//        val scheduleOutpatientItem: LinearLayout = findViewById(R.id.scheduleOutpatientItem)
//        val bookCounselingItem: LinearLayout = findViewById(R.id.bookCounselingItem)
       val inquiriesItem: LinearLayout = findViewById(R.id.dashInquiries)
        val faqsItem: LinearLayout = findViewById(R.id.faqsItem)

       ChildServicesMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
//
//        scheduleOutpatientItem.setOnClickListener {
//            // Handle schedule outpatient visit click
//            startActivity(Intent(this, ScheduleOutpatientActivity::class.java))
//        }
//
//        bookCounselingItem.setOnClickListener {
//            // Handle book counseling click
//            startActivity(Intent(this, BookCounselingActivity::class.java))
//        }
//
       inquiriesItem.setOnClickListener {
            // Handle inquiries click
            startActivity(Intent(this, CommentsActivity::class.java))
        }
//
//        faqsItem.setOnClickListener {
//            // Handle FAQs click
//            startActivity(Intent(this, FaqsActivity::class.java))
//        }

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Clear SharedPreferences
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Navigate to Login Activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
