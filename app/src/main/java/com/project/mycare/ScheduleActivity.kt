package com.project.mycare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.mycare.R
import java.util.*

class ScheduleAppointmentActivity : AppCompatActivity() {

    private lateinit var dbHelper: ScheduleDBHelper
    private lateinit var editTextPatientName: EditText
    private lateinit var editTextAppointmentDate: EditText
    private lateinit var editTextAppointmentTime: EditText
    private lateinit var editTextContactInfo: EditText
    private lateinit var buttonScheduleAppointment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scheduling)

        dbHelper = ScheduleDBHelper(this)

        editTextPatientName = findViewById(R.id.editTextPatientName)
        editTextAppointmentDate = findViewById(R.id.editTextAppointmentDate)
        editTextAppointmentTime = findViewById(R.id.editTextAppointmentTime)
        editTextContactInfo = findViewById(R.id.editTextContactInfo)
        buttonScheduleAppointment = findViewById(R.id.buttonScheduleAppointment)

        editTextAppointmentDate.setOnClickListener {
            showDatePicker()
        }

        editTextAppointmentTime.setOnClickListener {
            showTimePicker()
        }

        buttonScheduleAppointment.setOnClickListener {
            scheduleAppointment()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            editTextAppointmentDate.setText(date)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = "$selectedHour:$selectedMinute"
            editTextAppointmentTime.setText(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun scheduleAppointment() {
        val patientName = editTextPatientName.text.toString()
        val appointmentDate = editTextAppointmentDate.text.toString()
        val appointmentTime = editTextAppointmentTime.text.toString()
        val contactInfo = editTextContactInfo.text.toString()

        if (patientName.isEmpty() || appointmentDate.isEmpty() || appointmentTime.isEmpty() || contactInfo.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val isInserted = dbHelper.addAppointment(patientName, appointmentDate, appointmentTime, contactInfo)
            if (isInserted) {
                Toast.makeText(this, "Appointment scheduled successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this, "Failed to schedule appointment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        editTextPatientName.text.clear()
        editTextAppointmentDate.text.clear()
        editTextAppointmentTime.text.clear()
        editTextContactInfo.text.clear()
    }
}
