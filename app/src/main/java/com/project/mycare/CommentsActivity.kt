package com.project.mycare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
    private var selectedCommentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        commentsSection = findViewById(R.id.commentsSection)
        responseEditText = findViewById(R.id.responseEditText)
        responseButton = findViewById(R.id.responseButton)

        // Example list of comments
        val comments = listOf(
            Comment(1, "John Doe", "This is a sample comment."),
            Comment(2, "Jane Smith", "Another comment."),
            Comment(3, "Alice Johnson", "This is a third comment.")
        )

        for (comment in comments) {
            val commentView = LayoutInflater.from(this).inflate(R.layout.comment_item, null)

            val authorTextView: TextView = commentView.findViewById(R.id.commentAuthor)
            val commentTextView: TextView = commentView.findViewById(R.id.commentText)
            val ignoreButton: Button = commentView.findViewById(R.id.buttonIgnore)
            val reviewedButton: Button = commentView.findViewById(R.id.buttonReviewed)

            authorTextView.text = "Author: ${comment.author}"
            commentTextView.text = comment.text

            ignoreButton.setOnClickListener {
                // Handle ignore button click
                Toast.makeText(this, "Comment ignored.", Toast.LENGTH_SHORT).show()
            }

            reviewedButton.setOnClickListener {
                // Handle reviewed button click
                Toast.makeText(this, "Comment reviewed.", Toast.LENGTH_SHORT).show()
            }

            // Respond to comment when the comment is clicked
            commentView.setOnClickListener {
                selectedCommentId = comment.id
                responseEditText.hint = "Respond to ${comment.author}"
            }

            commentsSection.addView(commentView)
        }

        responseButton.setOnClickListener {
            val responseText = responseEditText.text.toString()
            if (responseText.isNotEmpty()) {
                // Handle the response (e.g., save to database or send to server)
                Toast.makeText(this, "Response: $responseText", Toast.LENGTH_SHORT).show()
                responseEditText.text.clear()
                responseEditText.hint = "Write a response..."
                selectedCommentId = null
            } else {
                Toast.makeText(this, "Please write a response.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

data class Comment(val id: Int, val author: String, val text: String)