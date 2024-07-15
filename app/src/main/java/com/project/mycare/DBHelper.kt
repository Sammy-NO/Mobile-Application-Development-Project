package com.project.mycare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserAccounts.db"
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_ROLE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(email: String, phoneNumber: String, username: String, password: String, role: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE_NUMBER, phoneNumber)
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_ROLE, role)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor: Cursor? = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val count = cursor?.count ?: 0
        cursor?.close()
        db.close()
        return count > 0
    }

    fun getUserRole(username: String): String? {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor? = db.query(TABLE_NAME, arrayOf(COLUMN_ROLE), selection, selectionArgs, null, null, null)
        var role: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
        }
        cursor?.close()
        db.close()
        return role
    }

    fun getCurrentUser(): User? {
        val db = this.readableDatabase
        var user: User? = null
        db.query(TABLE_NAME, arrayOf(COLUMN_USERNAME, COLUMN_EMAIL), null, null, null, null, null).use { cursor ->
            if (cursor.moveToFirst()) {
                user = User(
                    username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                )
            }
        }
        db.close()
        return user
    }
}