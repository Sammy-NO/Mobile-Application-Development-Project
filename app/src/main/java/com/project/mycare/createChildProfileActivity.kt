package com.project.mycare


import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.mycare.R

class CreateChildProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_child_profile)

        databaseHelper = DatabaseHelper(this)

        val editTextChildName: EditText = findViewById(R.id.editTextChildName)
        val editTextBirthDate: EditText = findViewById(R.id.editTextBirthDate)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val childName = editTextChildName.text.toString()
            val birthDate = editTextBirthDate.text.toString()

            if (childName.isNotEmpty() && birthDate.isNotEmpty()) {
                saveChildProfile(childName, birthDate)
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveChildProfile(name: String, birthDate: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put("user_id", 1)  // Assuming a single user or replace with actual user_id
            put("name", name)
            put("birth_date", birthDate)
        }
        val newRowId = db.insert("children", null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error creating profile", Toast.LENGTH_SHORT).show()
        }
    }
}