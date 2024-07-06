package com.cns.postnatalcare

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cns.postnatalcare.database.DatabaseHelper

class CreateVaccineProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private val choices = arrayOf("BCG", "OPV0", "OPV1", "OPV2", "ROTA1", "DPT1", "HepB1", "PCV1", "OPV2", "ROTA2", "DPT2", "HepB2", "PCV2", "OPV3", "IPV", "DPT3", "HepB3", "PCV3", "MR1", "YF-VAX","MR2" ) // Your actual vaccine choices
    private val selectedChoices = BooleanArray(choices.size)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_vaccine_profile) // Correct layout file

        databaseHelper = DatabaseHelper(this)
        val addTextChildName: EditText = findViewById(R.id.addTextChildName)
        val editAgeInWeeks: EditText = findViewById(R.id.editAgeInWeeks)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        val textViewSelectedChoices: TextView = findViewById(R.id.textViewSelectedChoices)
        val buttonSelectVaccines: Button = findViewById(R.id.buttonSelectVaccines)

        buttonSelectVaccines.setOnClickListener {
            showMultiSelectDialog()
        }

        buttonSave.setOnClickListener {
            val childName = addTextChildName.text.toString()
            val ageWeeksText = editAgeInWeeks.text.toString()
            val selectedVaccines = getSelectedVaccines()

            if (childName.isNotEmpty() && ageWeeksText.isNotBlank() && selectedVaccines.isNotEmpty()) {
                val ageWeeks = ageWeeksText.toIntOrNull()
                if (ageWeeks != null) {
                    saveVaccineProfile(childName, ageWeeks, selectedVaccines)
                } else {
                    Toast.makeText(this, "Please enter a valid number for age in weeks", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMultiSelectDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Vaccines")
        builder.setMultiChoiceItems(choices, selectedChoices) { _, which, isChecked ->
            selectedChoices[which] = isChecked
        }
        builder.setPositiveButton("OK") { _, _ ->
            val selectedItems = getSelectedVaccines()
            val textViewSelectedChoices: TextView = findViewById(R.id.textViewSelectedChoices)
            textViewSelectedChoices.text = selectedItems.joinToString(", ")
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun getSelectedVaccines(): List<String> {
        val selectedItems = mutableListOf<String>()
        for (i in choices.indices) {
            if (selectedChoices[i]) {
                selectedItems.add(choices[i])
            }
        }
        return selectedItems
    }

    private fun saveVaccineProfile(name: String, ageWeeks: Int, selectedVaccines: List<String>) {
        val db = databaseHelper.writableDatabase
        val allVaccines = getAllVaccines()

        for (vaccine in allVaccines) {
            val isSelected = selectedVaccines.contains(vaccine.name)
            val remainingTime = vaccine.weekToAdminister - ageWeeks

            val values = ContentValues().apply {
                put("vaccine_id", vaccine.id)
                put("status", if (isSelected) 1 else 0) // Use 1 for true and 0 for false
                put("remaining_time", remainingTime)
            }
            val newRowId = db.insert("profiles", null, values)

            if (newRowId == -1L) {
                Toast.makeText(this, "Error creating vaccine profile for ${vaccine.name}", Toast.LENGTH_SHORT).show()
                return
            }
        }
        Toast.makeText(this, "Vaccine profile created successfully", Toast.LENGTH_SHORT).show()
        finish()  // Close the activity after saving
    }

    private fun getAllVaccines(): List<Vaccine> {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("vaccines", null, null, null, null, null, null)
        val vaccines = mutableListOf<Vaccine>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("vaccine_id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val weekToAdminister = cursor.getInt(cursor.getColumnIndexOrThrow("week_to_administer"))
            vaccines.add(Vaccine(id, name, weekToAdminister))
        }
        cursor.close()
        return vaccines
    }

    data class Vaccine(val id: Int, val name: String, val weekToAdminister: Int)
}
