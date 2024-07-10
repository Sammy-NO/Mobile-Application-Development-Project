package com.project.mycare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var role: String // Variable to hold selected role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        dbHelper = DBHelper(this)

        val emailEditText = findViewById<EditText>(R.id.registeremail)
        val phoneNumberEditText = findViewById<EditText>(R.id.number)
        val usernameEditText = findViewById<EditText>(R.id.createusername)
        val passwordEditText = findViewById<EditText>(R.id.createpassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmpassword)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        val userRoleRadio = findViewById<RadioButton>(R.id.radioUser)
        val providerRoleRadio = findViewById<RadioButton>(R.id.radioHealthCareProvider)

        // Set default role
        role = "user" // Default to user role

        userRoleRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                role = "user"
            }
        }

        providerRoleRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                role = "healthcare provider"
            }
        }

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Perform basic validation
            if (email.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Add user to database
                val result = dbHelper.addUser(email, phoneNumber, username, password, role)
                if (result != -1L) {
                    Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                    // Navigate to login activity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginRedirect.setOnClickListener {
            // Navigate to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
