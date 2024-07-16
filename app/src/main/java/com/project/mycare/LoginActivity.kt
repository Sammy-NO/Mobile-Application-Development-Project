package com.project.mycare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val usernameEditText: EditText = findViewById(R.id.loginusername)
        val passwordEditText: EditText = findViewById(R.id.loginpassword)
        val loginButton: Button = findViewById(R.id.loginbutton)
        val dbHelper = DBHelper(this)
        val registerRedirect: TextView = findViewById(R.id.registerredirect)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password, dbHelper)
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerRedirect.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String, dbHelper: DBHelper) {
        if (dbHelper.authenticateUser(username, password)) {
            val role = dbHelper.getUserRole(username)
            val intent = when (role) {
                "User" -> Intent(this, Dash::class.java)
                "Healthcare Provider" -> Intent(this, DocDashActivity::class.java)
                else -> null
            }
            intent?.let {
                startActivity(it)
                finish()
            } ?: run {
                Toast.makeText(this, "Role not recognized", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}
