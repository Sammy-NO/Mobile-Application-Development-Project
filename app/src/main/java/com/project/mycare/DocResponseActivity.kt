package com.project.mycare

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CommentsActivity : AppCompatActivity() {

    private lateinit var commentsSection: LinearLayout
    private lateinit var responseEditText: EditText
    private lateinit var responseButton: Button
    private lateinit var dbHelper: CommentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        commentsSection = findViewById(R.id.commentsSection)
        responseEditText = findViewById(R.id.responseEditText)
        responseButton = findViewById(R.id.responseButton)
        dbHelper = CommentDBHelper(this)

        loadCommentsFromDB()

        responseButton.setOnClickListener {
            val responseText = responseEditText.text.toString()
            if (responseText.isNotEmpty()) {
                val commentId = dbHelper.addComment("", responseText) // Assuming empty contact info for response
                if (commentId != -1L) {
                    Toast.makeText(this, "Response added successfully.", Toast.LENGTH_SHORT).show()
                    responseEditText.text.clear()
                    loadCommentsFromDB()
                } else {
                    Toast.makeText(this, "Failed to add response.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a response.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCommentsFromDB() {
        val comments = dbHelper.getAllComments()

        commentsSection.removeAllViews()

        for (comment in comments) {
            val commentView = layoutInflater.inflate(R.layout.comment_item, null)

            val authorTextView: TextView = commentView.findViewById(R.id.commentAuthor)
            val commentTextView: TextView = commentView.findViewById(R.id.commentText)
            val respondButton: Button = commentView.findViewById(R.id.buttonRespond)

            authorTextView.text = "Author: ${comment.contactInfo}"
            commentTextView.text = comment.commentText

            respondButton.setOnClickListener {
                responseEditText.hint = "Respond to ${comment.contactInfo}"
            }

            commentsSection.addView(commentView)
        }
    }
}
