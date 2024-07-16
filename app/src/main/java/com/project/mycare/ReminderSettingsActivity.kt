package com.project.mycare

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import com.project.mycare.R
import com.project.mycare.ReminderReceiver
import java.util.*

class ReminderSettingsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)

        databaseHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

        val checkBoxEnableReminders: CheckBox = findViewById(R.id.checkBoxEnableReminders)
        val editTextReminderPeriod: EditText = findViewById(R.id.editTextReminderPeriod)
        val buttonSave: Button = findViewById(R.id.buttonSave)
        val imageViewTooltip: ImageView = findViewById(R.id.imageViewTooltip)

        // Set tooltip text
        imageViewTooltip.setOnClickListener {
            Toast.makeText(this, "Input the time before the due date on which you will receive the reminder,", Toast.LENGTH_LONG).show()
        }

        val isReminderEnabled = sharedPreferences.getBoolean("reminder_enabled", false)
        val reminderPeriod = sharedPreferences.getInt("reminder_period", 7)

        checkBoxEnableReminders.isChecked = isReminderEnabled
        editTextReminderPeriod.setText(reminderPeriod.toString())

        buttonSave.setOnClickListener {
            val enableReminders = checkBoxEnableReminders.isChecked
            val reminderPeriod = editTextReminderPeriod.text.toString().toInt()

            with(sharedPreferences.edit()) {
                putBoolean("reminder_enabled", enableReminders)
                putInt("reminder_period", reminderPeriod)
                apply()
            }

            if (enableReminders) {
                scheduleReminders(reminderPeriod)
            } else {
                cancelReminders()
            }
        }
    }

    private fun scheduleReminders(reminderPeriod: Int) {
        val profiles = getAllProfiles()
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (profile in profiles) {
            val vaccineName = getVaccineName(profile.vaccineId)
            val reminderTime = profile.remainingTime - reminderPeriod
            if (reminderTime > 0) {
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.WEEK_OF_YEAR, reminderTime)
                }
                val intent = ReminderReceiver.createIntent(this, profile.childId, vaccineName)
                val pendingIntent = PendingIntent.getBroadcast(this, profile.profileId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    private fun cancelReminders() {
        val profiles = getAllProfiles()
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (profile in profiles) {
            val vaccineName = getVaccineName(profile.vaccineId)
            val intent = ReminderReceiver.createIntent(this, profile.childId, vaccineName)
            val pendingIntent = PendingIntent.getBroadcast(this, profile.profileId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun getAllProfiles(): List<Profile> {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("profiles", null, null, null, null, null, null)
        val profiles = mutableListOf<Profile>()
        while (cursor.moveToNext()) {
            val profileId = cursor.getInt(cursor.getColumnIndexOrThrow("profile_id"))
            val childId = cursor.getInt(cursor.getColumnIndexOrThrow("child_id"))
            val vaccineId = cursor.getInt(cursor.getColumnIndexOrThrow("vaccine_id"))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
            val remainingTime = cursor.getInt(cursor.getColumnIndexOrThrow("remaining_time"))
            profiles.add(Profile(profileId, childId, vaccineId, status, remainingTime))
        }
        cursor.close()
        return profiles
    }

    private fun getVaccineName(vaccineId: Int): String {
        val db = databaseHelper.readableDatabase
        val cursor = db.query("vaccines", arrayOf("name"), "vaccine_id = ?", arrayOf(vaccineId.toString()), null, null, null)
        var vaccineName = ""
        if (cursor.moveToFirst()) {
            vaccineName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }
        cursor.close()
        return vaccineName
    }

    data class Profile(val profileId: Int, val childId: Int, val vaccineId: Int, val status: Int, val remainingTime: Int)
}
