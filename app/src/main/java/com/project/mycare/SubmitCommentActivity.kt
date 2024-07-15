package com.project.mycare

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SubmitCommentActivity : AppCompatActivity() {

    private lateinit var commentInput: EditText
    private lateinit var contactInfoInput: EditText
    private lateinit var submitCommentButton: Button
    private lateinit var dbHelper: CommentDBHelper // Changed to CommentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_comment)

        commentInput = findViewById(R.id.commentInput)
        contactInfoInput = findViewById(R.id.contactInput)
        submitCommentButton = findViewById(R.id.submitCommentButton)
        dbHelper = CommentDBHelper(this)

        submitCommentButton.setOnClickListener {
            val contactInfo = contactInfoInput.text.toString().trim()
            val commentText = commentInput.text.toString().trim()

            if (contactInfo.isNotEmpty() && commentText.isNotEmpty()) {
                submitComment(contactInfo, commentText)
            } else {
                Toast.makeText(this, "Please enter contact info and comment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitComment(contactInfo: String, commentText: String) {
        val result = dbHelper.addComment(contactInfo, commentText)
        if (result != -1L) {
            Toast.makeText(this, "Comment successfully submitted", Toast.LENGTH_SHORT).show()
            commentInput.text.clear()
            contactInfoInput.text.clear()
        } else {
            Toast.makeText(this, "Failed to submit comment", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        dbHelper.close() // Close the DBHelper when the activity is destroyed
        super.onDestroy()
    }
}
