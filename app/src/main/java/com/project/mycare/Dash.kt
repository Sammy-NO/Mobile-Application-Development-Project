package com.project.mycare

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


        if (currentUser != null) {
            usernameTextView.text = currentUser.username
            userEmailTextView.text = currentUser.email
        } else {
            usernameTextView.text = getString(R.string.unknown_dash_message)
            userEmailTextView.text = getString(R.string.no_email_dash_message)
        }

        // Set Dashboard Items
        setupDashboardItems()
    }

    private fun setupDashboardItems() {
        val ChildServicesMenu: LinearLayout = findViewById(R.id.childservices)
        val scheduleOutpatientItem: LinearLayout = findViewById(R.id.scheduleVisit)
        val bookCounselingItem: LinearLayout = findViewById(R.id.bookTherapy)
        val inquiriesItem: LinearLayout = findViewById(R.id.dashInquiries)
        val faqsItem: LinearLayout = findViewById(R.id.faqlist)

       ChildServicesMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
      scheduleOutpatientItem.setOnClickListener {
          startActivity(Intent(this, ScheduleAppointmentActivity::class.java))
      }
        bookCounselingItem.setOnClickListener {
           startActivity(Intent(this, TherapyAppointmentActivity::class.java))
       }
       inquiriesItem.setOnClickListener {
           startActivity(Intent(this, SubmitCommentActivity::class.java))
        }
        faqsItem.setOnClickListener {
            startActivity(Intent(this, FAQActivity::class.java))
        }

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
