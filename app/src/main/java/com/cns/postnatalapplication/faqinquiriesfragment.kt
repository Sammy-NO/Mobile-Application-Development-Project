package com.cns.postnatalapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FaqInquiriesFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faq_inquiries, container, false)

        val faqListView = view.findViewById<ListView>(R.id.faqListView)
        val inquiryEditText = view.findViewById<EditText>(R.id.inquiryEditText)
        val sendInquiryButton = view.findViewById<Button>(R.id.sendInquiryButton)

        // Sample FAQs
        val faqs = listOf(
            "What is postnatal care?",
            "How often should I visit the doctor after delivery?",
            "What should I do if I feel depressed?"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, faqs)
        faqListView.adapter = adapter

        sendInquiryButton.setOnClickListener {
            val inquiry = inquiryEditText.text.toString()
            if (inquiry.isNotBlank()) {
                Toast.makeText(context, "Inquiry sent: $inquiry", Toast.LENGTH_SHORT).show()
                inquiryEditText.text.clear()
            } else {
                Toast.makeText(context, "Please enter your inquiry", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
