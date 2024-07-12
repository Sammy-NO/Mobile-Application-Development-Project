package com.cns.postnatalcare

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.TooltipCompat
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import com.cns.postnatalcare.database.DatabaseHelper
import java.util.*

class ReminderSettingsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinnerSelectChild: Spinner
    private lateinit var children: List<Child>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)

        databaseHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

        spinnerSelectChild = findViewById(R.id.spinnerSelectChild)
        val checkBoxEnableReminders: CheckBox = findViewById(R.id.checkBoxEnableReminders)
        val editTextReminderPeriod: EditText = findViewById(R.id.editTextReminderPeriod)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        loadChildrenIntoSpinner()

        val isReminderEnabled = sharedPreferences.getBoolean("reminder_enabled", false)
        val reminderPeriod = sharedPreferences.getInt("reminder_period", 7)

        checkBoxEnableReminders.isChecked = isReminderEnabled
        editTextReminderPeriod.setText(reminderPeriod.toString())

        buttonSave.setOnClickListener {
            val enableReminders = checkBoxEnableReminders.isChecked
            val reminderPeriod = editTextReminderPeriod.text.toString().toInt()
            val selectedChild = children[spinnerSelectChild.selectedItemPosition]

            with(sharedPreferences.edit()) {
                putBoolean("reminder_enabled", enableReminders)
                putInt("reminder_period", reminderPeriod)
                putInt("selected_child_id", selectedChild.childId)
                apply()
            }

            if (enableReminders) {
                scheduleReminders(reminderPeriod, selectedChild.childId)
            } else {
                cancelReminders(selectedChild.childId)
            }
        }
    }

    private fun loadChildrenIntoSpinner() {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("children", null, null, null, null, null, null)
        children = mutableListOf()
        val childNames = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val childId = cursor.getInt(cursor.getColumnIndexOrThrow("child_id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            (children as MutableList<Child>).add(Child(childId, name))
            childNames.add(name)
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, childNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectChild.adapter = adapter
    }

    private fun scheduleReminders(reminderPeriod: Int, childId: Int) {
        val profiles = getProfilesForChild(childId)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (profile in profiles) {
            val reminderTime = profile.remainingTime - reminderPeriod
            if (reminderTime > 0) {
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.WEEK_OF_YEAR, reminderTime)
                }
                val intent = ReminderReceiver.createIntent(this, profile.childId, profile.vaccineName)
                val pendingIntent = PendingIntent.getBroadcast(this, profile.profileId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    private fun cancelReminders(childId: Int) {
        val profiles = getProfilesForChild(childId)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (profile in profiles) {
            val intent = ReminderReceiver.createIntent(this, profile.childId, profile.vaccineName)
            val pendingIntent = PendingIntent.getBroadcast(this, profile.profileId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun getProfilesForChild(childId: Int): List<Profile> {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("profiles", null, "child_id=?", arrayOf(childId.toString()), null, null, null)
        val profiles = mutableListOf<Profile>()
        while (cursor.moveToNext()) {
            val profileId = cursor.getInt(cursor.getColumnIndexOrThrow("profile_id"))
            val vaccineId = cursor.getInt(cursor.getColumnIndexOrThrow("vaccine_id"))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
            val remainingTime = cursor.getInt(cursor.getColumnIndexOrThrow("remaining_time"))
            val vaccineName = getVaccineNameById(vaccineId)
            profiles.add(Profile(profileId, childId, vaccineId, vaccineName, status, remainingTime))
        }
        cursor.close()
        return profiles
    }

    private fun getVaccineNameById(vaccineId: Int): String {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("vaccines", arrayOf("name"), "vaccine_id=?", arrayOf(vaccineId.toString()), null, null, null)
        cursor.moveToFirst()
        val vaccineName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        cursor.close()
        return vaccineName
    }

    data class Child(val childId: Int, val name: String)
    data class Profile(val profileId: Int, val childId: Int, val vaccineId: Int, val vaccineName: String, val status: Int, val remainingTime: Int)
}
