package com.cns.postnatalapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class AppointmentRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertAppointment(patientName: String, patientAge: Int, doctorName: String, appointmentDate: String): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PATIENT_NAME, patientName)
            put(DatabaseHelper.COLUMN_PATIENT_AGE, patientAge)
            put(DatabaseHelper.COLUMN_DOCTOR_NAME, doctorName)
            put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, appointmentDate)
        }
        return db.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values)
    }

    fun getAllAppointments(): List<Appointment> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query(DatabaseHelper.TABLE_APPOINTMENTS, null, null, null, null, null, null)
        val appointments = mutableListOf<Appointment>()
        with(cursor) {
            while (moveToNext()) {
                val appointment = Appointment(
                    getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATIENT_NAME)),
                    getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATIENT_AGE)),
                    getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_NAME)),
                    getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_DATE))
                )
                appointments.add(appointment)
            }
        }
        cursor.close()
        return appointments
    }
}
