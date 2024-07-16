package com.project.mycare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "PostNatalApplication.db"
        const val TABLE_NAME = "Appointments"
        const val COLUMN_ID = "id"
        const val COLUMN_PATIENT_NAME = "patient_name"
        const val COLUMN_APPOINTMENT_DATE = "appointment_date"
        const val COLUMN_APPOINTMENT_TIME = "appointment_time"
        const val COLUMN_CONTACT_INFO = "contact_info"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_APPOINTMENTS_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_PATIENT_NAME TEXT,"
                + "$COLUMN_APPOINTMENT_DATE TEXT,"
                + "$COLUMN_APPOINTMENT_TIME TEXT,"
                + "$COLUMN_CONTACT_INFO TEXT" + ")")
        db?.execSQL(CREATE_APPOINTMENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addAppointment(patientName: String, appointmentDate: String, appointmentTime: String, contactInfo: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PATIENT_NAME, patientName)
            put(COLUMN_APPOINTMENT_DATE, appointmentDate)
            put(COLUMN_APPOINTMENT_TIME, appointmentTime)
            put(COLUMN_CONTACT_INFO, contactInfo)
        }
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (success != -1L)
    }

    fun getAppointments(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}
