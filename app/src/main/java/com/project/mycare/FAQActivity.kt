package com.project.mycare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FAQActivity : AppCompatActivity() {

    private lateinit var faqRecyclerView: RecyclerView
    private lateinit var faqAdapter: FAQAdapter
    private lateinit var faqDBHelper: FAQDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_list)

        faqRecyclerView = findViewById(R.id.recyclerViewFAQ)
        faqRecyclerView.layoutManager = LinearLayoutManager(this)

        faqDBHelper = FAQDBHelper(this)
        val faqList = faqDBHelper.getAllFAQs()

        faqAdapter = FAQAdapter(faqList)
        faqRecyclerView.adapter = faqAdapter
    }
}
