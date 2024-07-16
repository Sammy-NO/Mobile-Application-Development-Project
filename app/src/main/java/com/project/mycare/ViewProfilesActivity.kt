package com.project.mycare

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.mycare.R

class ViewProfilesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var spinnerChildName: Spinner
    private lateinit var recyclerViewProfiles: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter
    private val children = mutableListOf<Child>()
    private val profiles = mutableListOf<Profile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profiles)

        databaseHelper = DatabaseHelper(this)
        spinnerChildName = findViewById(R.id.spinnerChildName)
        recyclerViewProfiles = findViewById(R.id.recyclerViewProfiles)

        profileAdapter = ProfileAdapter(profiles)
        recyclerViewProfiles.layoutManager = LinearLayoutManager(this)
        recyclerViewProfiles.adapter = profileAdapter

        populateChildSpinner()
    }

    private fun populateChildSpinner() {
        children.addAll(getAllChildren())
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, children)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChildName.adapter = adapter

        spinnerChildName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedChild = parent.getItemAtPosition(position) as? Child
                if (selectedChild != null) {
                    loadProfiles(selectedChild.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun loadProfiles(childId: Int) {
        profiles.clear()
        profiles.addAll(getProfilesByChildId(childId))
        if (profiles.isEmpty()) {
            Toast.makeText(this, "No profile found for this child. Please create a profile first.", Toast.LENGTH_SHORT).show()
        }
        profileAdapter.notifyDataSetChanged()
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

    private fun getProfilesByChildId(childId: Int): List<Profile> {
        val db = databaseHelper.readableDatabase
        val query = """
            SELECT profiles.profile_id, profiles.vaccine_id, profiles.status, profiles.remaining_time, vaccines.name AS vaccine_name
            FROM profiles
            JOIN vaccines ON profiles.vaccine_id = vaccines.vaccine_id
            WHERE profiles.child_id = ?
        """
        val cursor = db.rawQuery(query, arrayOf(childId.toString()))
        val profiles = mutableListOf<Profile>()
        while (cursor.moveToNext()) {
            val profileId = cursor.getInt(cursor.getColumnIndexOrThrow("profile_id"))
            val vaccineId = cursor.getInt(cursor.getColumnIndexOrThrow("vaccine_id"))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
            val remainingTime = cursor.getInt(cursor.getColumnIndexOrThrow("remaining_time"))
            val vaccineName = cursor.getString(cursor.getColumnIndexOrThrow("vaccine_name"))
            profiles.add(Profile(profileId, childId, vaccineId, vaccineName, status, remainingTime))
        }
        cursor.close()
        return profiles
    }

    data class Child(val id: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }

    data class Profile(val profileId: Int, val childId: Int, val vaccineId: Int, val vaccineName: String, val status: Int, val remainingTime: Int)
}
