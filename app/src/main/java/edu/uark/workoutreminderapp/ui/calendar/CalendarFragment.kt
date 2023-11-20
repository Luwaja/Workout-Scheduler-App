package edu.uark.workoutreminderapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.databinding.FragmentCalendarBinding
import edu.uark.workoutreminderapp.model.Workout
import androidx.fragment.app.viewModels
import edu.uark.workoutreminderapp.WorkoutApplication

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val calendarViewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val calendarViewModel =
//            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCalendar
        val calendarView: CalendarView = binding.calendar

        calendarViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // This should go through each workout in the database and call the drawWorkoutsOnCalendar for it.
        calendarViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            workouts.let {
                drawWorkoutsOnCalendar(it)
            }
        }
        return root
    }

    private fun drawWorkoutsOnCalendar(workoutList: List<Workout>) {
        for (workout in workoutList) {
            //TODO: Make calendar change colors for workouts completed, missed, upcoming.
            //NOTE: Found it it's almost impossible to change the background color of default CalendarView.
            // one possible idea is to use a third-party calendar "MaterialCalendarView" from applandeo library (if we can).
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}