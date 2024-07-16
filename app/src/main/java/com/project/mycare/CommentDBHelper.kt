package com.project.mycare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CommentDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CommentDatabase.db"
        private const val TABLE_COMMENTS = "comments"
        private const val COLUMN_COMMENT_ID = "comment_id"
        private const val COLUMN_CONTACT_INFO = "contact_info"
        private const val COLUMN_COMMENT = "comment"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createCommentsTableQuery = "CREATE TABLE $TABLE_COMMENTS (" +
                "$COLUMN_COMMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CONTACT_INFO TEXT," +
                "$COLUMN_COMMENT TEXT)"

        db?.execSQL(createCommentsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COMMENTS")
        onCreate(db)
    }

    fun addComment(contactInfo: String, commentText: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CONTACT_INFO, contactInfo)
            put(COLUMN_COMMENT, commentText)
        }
        val result = db.insert(TABLE_COMMENTS, null, contentValues)
        db.close()
        return result
    }

    fun getAllComments(): List<Comment> {
        val commentsList = mutableListOf<Comment>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_COMMENTS"
        val cursor: Cursor? = db.rawQuery(query, null)
        cursor?.use {
            while (it.moveToNext()) {
                val commentId = it.getLong(it.getColumnIndexOrThrow(COLUMN_COMMENT_ID))
                val contactInfo = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTACT_INFO))
                val commentText = it.getString(it.getColumnIndexOrThrow(COLUMN_COMMENT))
                val comment = Comment(commentId.toInt(), contactInfo, commentText)
                commentsList.add(comment)
            }
        }
        cursor?.close()
        db.close()
        return commentsList
    }
}