package edu.uark.workoutreminderapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.databinding.FragmentHomeBinding
import edu.uark.workoutreminderapp.WorkoutApplication
import androidx.fragment.app.viewModels
import edu.uark.workoutreminderapp.NotificationUtil
import edu.uark.workoutreminderapp.ui.addworkout.AddWorkoutFragment
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import edu.uark.workoutreminderapp.R
import androidx.navigation.fragment.findNavController
import edu.uark.workoutreminderapp.model.Workout
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val firstWorkoutButton: AppCompatButton = binding.upcomingExample1
        val secondWorkoutButton: AppCompatButton = binding.upcomingExample2
        val activeWorkoutButton: AppCompatButton = binding.viewUpcoming

        var firstWorkout: Workout? = null
        var secondWorkout: Workout? = null
        var activeWorkout: Workout? = null

//        val newWorkoutButton: AppCompatButton = findViewById(R.id.view_new_workout)
//        newWorkoutButton.setOnClickListener {
//            // Handle button click for new workout
//        }

        val newWorkoutButton: AppCompatButton = binding.viewNewWorkout
        newWorkoutButton.setOnClickListener {
            Log.d("HomeFragment", "Launching Add Workout Fragment.")
            findNavController().navigate(R.id.navigation_addworkout)
        }

        // Schedule the notifications for each workout.
        homeViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            workouts.let {
                if (it.size > 0) {
                    it[0].id?.let { it1 ->
                        NotificationUtil().createClickableNotification(
                            requireContext(),
                            it[0].name,
                            it[0].category,
                            // This may or may not work "requireContext()".
                            Intent(requireContext(), AddWorkoutFragment::class.java),
                            it1
                        )
                    }
                }
            }
        }

        // Finds the new two workouts coming up.
        homeViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            if (workouts.size >= 3) {
                activeWorkout = workouts[0]
                firstWorkout = workouts[1]
                secondWorkout = workouts[2]
            } else if (workouts.size == 2) {
                activeWorkout = workouts[0]
                secondWorkout = workouts[1]
            } else if (workouts.size == 1) {
                activeWorkout = workouts[0]
            }
        }

        // Set the buttons on the home view to have the text of the workouts.
        // If there is a active workout, change the button color to red.
        if (activeWorkout != null) {
            val date = Date(activeWorkout!!.date)
            activeWorkoutButton.text = "${activeWorkout!!.name}      ${formatDate(date)}"
            if (System.currentTimeMillis()-3600000 <= activeWorkout!!.date && activeWorkout!!.date <= System.currentTimeMillis()+3600000) {
                activeWorkoutButton.setBackgroundColor(Color.parseColor("#8E0000"))
            }
            activeWorkoutButton.setOnClickListener {
                // TODO: Launch Fragment with activeWorkout!!.id passed to open the add workouts page for that workout.
            }
        } else {
            activeWorkoutButton.text = "No Workout Found."
        }

        if (firstWorkout != null) {
            val date = Date(firstWorkout!!.date)
            firstWorkoutButton.text = "${firstWorkout!!.name}      ${formatDate(date)}"
            firstWorkoutButton.setOnClickListener {
                // TODO: Launch Fragment with activeWorkout!!.id passed to open the add workouts page for that workout.
            }
        } else {
            firstWorkoutButton.text = "No Workout Found."
        }

        if (secondWorkout != null) {
            val date = Date(secondWorkout!!.date)
            secondWorkoutButton.text = "${secondWorkout!!.name}      ${formatDate(date)}"
            secondWorkoutButton.setOnClickListener {
                // TODO: Launch Fragment with activeWorkout!!.id passed to open the add workouts page for that workout.
            }
        } else {
            secondWorkoutButton.text = "No Workout Found."
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(date: Date): String {
        val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return formattedDate.format(date)
    }
}