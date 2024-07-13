package com.cns.postnatalapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "postnatalapp.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_APPOINTMENTS = "appointments"
        const val COLUMN_ID = "id"
        const val COLUMN_PATIENT_NAME = "patient_name"
        const val COLUMN_PATIENT_AGE = "patient_age"
        const val COLUMN_DOCTOR_NAME = "doctor_name"
        const val COLUMN_APPOINTMENT_DATE = "appointment_date"
    }

    private val CREATE_TABLE_APPOINTMENTS = (
            "CREATE TABLE $TABLE_APPOINTMENTS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PATIENT_NAME TEXT, " +
                    "$COLUMN_PATIENT_AGE INTEGER, " +
                    "$COLUMN_DOCTOR_NAME TEXT, " +
                    "$COLUMN_APPOINTMENT_DATE TEXT)"
            )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_APPOINTMENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        onCreate(db)
    }
}
