# Mobile-Application-Development-Project
Shared Repo for the app being created as a group project.


Here's the Kotlin code to create separate views for a doctor's upcoming activities and booking counseling/therapy sessions:

1. Upcoming Activities View:

class DoctorUpcomingActivitiesFragment : Fragment() {

    private lateinit var viewModel: DoctorUpcomingActivitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_upcoming_activities, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(DoctorUpcomingActivitiesViewModel::class.java)

        val upcomingActivitiesList = view.findViewById<RecyclerView>(R.id.upcoming_activities_list)
        upcomingActivitiesList.adapter = UpcomingActivitiesAdapter(requireContext())

        viewModel.upcomingActivities.observe(viewLifecycleOwner, Observer { activities ->
            (upcomingActivitiesList.adapter as UpcomingActivitiesAdapter).updateData(activities)
        })

        return view
    }
}

class UpcomingActivitiesAdapter(context: Context) : RecyclerView.Adapter<UpcomingActivitiesViewHolder>() {

    private var activities = emptyList<UpcomingActivity>()

    internal fun updateData(updatedActivities: List<UpcomingActivity>) {
        activities = updatedActivities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingActivitiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_activity, parent, false)
        return UpcomingActivitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingActivitiesViewHolder, position: Int) {
        val activity = activities[position]
        holder.titleTextView.text = activity.title
        holder.dateTextView.text = activity.date
        holder.timeTextView.text = activity.time
    }

    override fun getItemCount(): Int = activities.size
}

data class UpcomingActivity(val title: String, val date: String, val time: String)

class UpcomingActivitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
    val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
    val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)
}
2. Booking Counseling/Therapy View:

class BookingTherapyFragment : Fragment() {

    private lateinit var viewModel: BookingTherapyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking_therapy, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(BookingTherapyViewModel::class.java)

        val patientSpinner = view.findViewById<Spinner>(R.id.patient_spinner)
        val appointmentTypeSpinner = view.findViewById<Spinner>(R.id.appointment_type_spinner)
        val datePicker = view.findViewById<DatePicker>(R.id.date_picker)
        val startTimePicker = view.findViewById<TimePicker>(R.id.start_time_picker)
        val endTimePicker = view.findViewById<TimePicker>(R.id.end_time_picker)
        val bookButton = view.findViewById<Button>(R.id.book_button)

        // ... Populate spinners with patient and appointment type data (implementation omitted)

        bookButton.setOnClickListener {
            val selectedPatient = patientSpinner.selectedItem as String
            val appointmentType = appointmentTypeSpinner.selectedItem as String
            val selectedDate = datePicker.date
            val startTime = startTimePicker.hour to startTimePicker.minute
            val endTime = endTimePicker.hour to endTimePicker.minute

            viewModel.bookAppointment(selectedPatient, appointmentType, selectedDate, startTime, endTime)
        }

        return view
    }
}

class BookingTherapyViewModel(private val appointmentRepository: AppointmentRepository) : ViewModel() {

    fun bookAppointment(
        patientId: String,
        appointmentType: String,
        date: Long,
        startTime: Pair<Int, Int>,
        endTime: Pair<Int, Int>
    ) {
        val appointment = Appointment(patientId, appointmentType, date, startTime, endTime)
        appointmentRepository.bookAppointment(appointment)
    }
}

// Interface for Appointment Repository (implementation omitted for brevity)
interface AppointmentRepository {
    fun bookAppointment(appointment: Appointment)
}
