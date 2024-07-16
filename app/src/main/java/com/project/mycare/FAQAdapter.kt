package com.project.mycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FAQAdapter(private val faqList: List<Pair<String, String>>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FAQViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq = faqList[position]
        holder.bind(faq.first, faq.second)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        private val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)

        fun bind(question: String, answer: String) {
            questionTextView.text = question
            answerTextView.text = answer
        }
    }
}
