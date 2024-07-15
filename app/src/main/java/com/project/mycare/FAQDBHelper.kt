// FAQDBHelper.kt
package com.project.mycare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FAQDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FAQDatabase.db"
        private const val TABLE_FAQS = "faqs"
        private const val COLUMN_FAQ_ID = "faq_id"
        private const val COLUMN_QUESTION = "question"
        private const val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_FAQS (" +
                "$COLUMN_FAQ_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_QUESTION TEXT," +
                "$COLUMN_ANSWER TEXT)"

        db?.execSQL(createTableQuery)
        insertInitialFAQs(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAQS")
        onCreate(db)
    }

    fun getAllFAQs(): List<Pair<String, String>> {
        val faqList = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_FAQS"
        val cursor: Cursor? = db.rawQuery(query, null)

        cursor?.use {
            while (it.moveToNext()) {
                val question = it.getString(it.getColumnIndexOrThrow(COLUMN_QUESTION))
                val answer = it.getString(it.getColumnIndexOrThrow(COLUMN_ANSWER))
                faqList.add(Pair(question, answer))
            }
        }
        cursor?.close()
        db.close()

        return faqList
    }

    private fun insertInitialFAQs(db: SQLiteDatabase?) {
        val faqList = listOf(
            Pair("What vaccinations does my child need?",
                "Children need vaccinations to protect against diseases like measles, mumps, rubella, and more. " +
                        "Consult your pediatrician for a complete vaccination schedule."),
            Pair("How do I handle my child's fever?",
                "You can manage a child's fever with over-the-counter medications like acetaminophen or " +
                        "ibuprofen, plenty of fluids, and rest. Seek medical advice for high or persistent fevers."),
            Pair("What are signs of a developmental delay?",
                "Signs include delayed speech, motor skills, social skills, and learning abilities. Early intervention " +
                        "is crucial; talk to your child's doctor if you have concerns."),
            Pair("How can I help my child with school anxiety?",
                "Create a supportive environment, encourage open communication, practice relaxation techniques, " +
                        "and involve school staff in addressing anxiety triggers."),
            Pair("When should my child start solid foods?",
                "Around 6 months, when your baby shows signs of readiness like sitting up with support and showing " +
                        "interest in food. Start with simple, single-ingredient foods.")
            // Add more FAQs as needed
        )

        faqList.forEach { (question, answer) ->
            val contentValues = ContentValues().apply {
                put(COLUMN_QUESTION, question)
                put(COLUMN_ANSWER, answer)
            }
            db?.insert(TABLE_FAQS, null, contentValues)
        }
    }
}
