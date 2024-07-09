package com.cns.postnatalcare

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cns.postnatalcare.database.DatabaseHelper

class CreateVaccineProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var spinnerChildName: Spinner
    private val choices = arrayOf("BCG", "OPV0", "OPV1", "OPV2", "ROTA1", "DPT1", "HepB1", "PCV1", "OPV2", "ROTA2", "DPT2", "HepB2", "PCV2", "OPV3", "IPV", "DPT3", "HepB3", "PCV3", "MR1", "YF-VAX", "MR2") // Your actual vaccine choices
    private val selectedChoices = BooleanArray(choices.size)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_vaccine_profile) // Correct layout file

        databaseHelper = DatabaseHelper(this)
        spinnerChildName = findViewById(R.id.spinnerChildName)
        val editAgeInWeeks: EditText = findViewById(R.id.editAgeInWeeks)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        val textViewSelectedChoices: TextView = findViewById(R.id.textViewSelectedChoices)
        val buttonSelectVaccines: Button = findViewById(R.id.buttonSelectVaccines)

        populateChildSpinner()

        buttonSelectVaccines.setOnClickListener {
            showMultiSelectDialog()
        }

        buttonSave.setOnClickListener {
            val selectedChild = spinnerChildName.selectedItem as? Child
            val ageWeeksText = editAgeInWeeks.text.toString()
            val selectedVaccines = getSelectedVaccines()

            if (selectedChild != null && ageWeeksText.isNotBlank() && selectedVaccines.isNotEmpty()) {
                val ageWeeks = ageWeeksText.toIntOrNull()
                if (ageWeeks != null) {
                    saveVaccineProfile(selectedChild.id, ageWeeks, selectedVaccines)
                } else {
                    Toast.makeText(this, "Please enter a valid number for age in weeks", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateChildSpinner() {
        val children = getAllChildren()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, children)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChildName.adapter = adapter
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

    private fun saveVaccineProfile(childId: Int, ageWeeks: Int, selectedVaccines: List<String>) {
        val db = databaseHelper.writableDatabase
        val allVaccines = getAllVaccines()

        for (vaccine in allVaccines) {
            val isSelected = selectedVaccines.contains(vaccine.name)
            val remainingTime = vaccine.weekToAdminister - ageWeeks

            val values = ContentValues().apply {
                put("child_id", childId)
                put("vaccine_id", vaccine.id)
                put("status", if (isSelected) 0 else 1)
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

    private fun getAllChildren(): List<Child> {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("children", null, null, null, null, null, null)
        val children = mutableListOf<Child>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("child_id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            children.add(Child(id, name))
        }
        cursor.close()
        return children
    }

    data class Vaccine(val id: Int, val name: String, val weekToAdminister: Int)
    data class Child(val id: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }
}
