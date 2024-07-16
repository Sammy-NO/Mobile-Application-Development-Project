package com.project.mycare

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.mycare.R

class ProfileAdapter(private val profiles: List<ViewProfilesActivity.Profile>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.vaccineNameTextView.text = profile.vaccineName
        holder.statusTextView.text = if (profile.status == 1) "Received" else "Not yet received"
        holder.statusTextView.setTextColor(if (profile.status == 1) Color.GREEN else Color.RED)
        holder.remainingTimeTextView.text = (if (profile.status == 0) "Remaining time: ${profile.remainingTime} weeks" else "Remaining time : Not available")
        //holder.remainingTimeTextView.text = "Remaining time: ${profile.remainingTime} weeks"
    }

    override fun getItemCount() = profiles.size

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vaccineNameTextView: TextView = itemView.findViewById(R.id.textViewVaccineName)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val remainingTimeTextView: TextView = itemView.findViewById(R.id.textViewRemainingTime)
    }
}
