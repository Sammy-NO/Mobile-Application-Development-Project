package com.cns.postnatalapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class OutpatientSchedulingFragment : Fragment() {

    private lateinit var appointmentRepository: AppointmentRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_outpatient_schedhuling, container, false)

        appointmentRepository = AppointmentRepository(requireContext())

        val confirmButton = view.findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val patientName = view.findViewById<EditText>(R.id.patientName).text.toString()
            val patientAge = view.findViewById<EditText>(R.id.patientAge).text.toString().toInt()
            val doctorName = view.findViewById<Spinner>(R.id.doctorSpinner).selectedItem.toString()
            val appointmentDate = view.findViewById<CalendarView>(R.id.calendarView).date.toString()

            appointmentRepository.insertAppointment(patientName, patientAge, doctorName, appointmentDate)
            Toast.makeText(context, "Appointment scheduled!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
