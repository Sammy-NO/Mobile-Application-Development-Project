package com.project.mycare

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TherapyDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TherapyAppointments.db"
        const val TABLE_NAME = "TherapyAppointments"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_CONTACT = "contact"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_THERAPY_APPOINTMENTS_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_DATE TEXT,"
                + "$COLUMN_TIME TEXT,"
                + "$COLUMN_CONTACT TEXT" + ")")
        db?.execSQL(CREATE_THERAPY_APPOINTMENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTherapyAppointment(name: String, date: String, time: String, contact: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_TIME, time)
        values.put(COLUMN_CONTACT, contact)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success != -1L
    }
}
