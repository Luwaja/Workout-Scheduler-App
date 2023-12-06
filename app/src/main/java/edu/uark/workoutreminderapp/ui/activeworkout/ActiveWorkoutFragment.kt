package edu.uark.workoutreminderapp.ui.activeworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.uark.workoutreminderapp.R
import edu.uark.workoutreminderapp.WorkoutApplication
import edu.uark.workoutreminderapp.databinding.FragmentActiveworkoutBinding
import edu.uark.workoutreminderapp.model.Workout
import edu.uark.workoutreminderapp.ui.addworkout.AddWorkoutViewModel
import edu.uark.workoutreminderapp.ui.addworkout.AddWorkoutViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ActiveWorkoutFragment : Fragment() {
    private var _binding: FragmentActiveworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var editSets: EditText
    private lateinit var editTitle: TextView
    private lateinit var editCategory: TextView

    private val activeWorkoutViewModel: ActiveWorkoutViewModel by viewModels {
        ActiveWorkoutViewModelFactory((requireActivity().application as WorkoutApplication).repository, -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val activeWorkoutViewModel =
//            ViewModelProvider(this).get(ActiveWorkoutViewModel::class.java)

        _binding = FragmentActiveworkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = arguments
        var id = args?.getInt("ID")
        if (id == null) {
            id = -1
        }

        if (id != -1) {
            activeWorkoutViewModel.updateId(id)
        }

        editSets = binding.activeSets
        editTitle = binding.activeTitle
        editCategory = binding.activeCategory

        // TODO: Uncomment this when button added.
//        val btnSave = binding.btnSave
//        btnSave.setOnClickListener {
//            CoroutineScope(SupervisorJob()).launch {
//                getWorkoutFromUI()?.let { workout -> activeWorkoutViewModel.update(workout) }
//            }
//            findNavController().navigate(R.id.navigation_home)
//        }


        activeWorkoutViewModel.curWorkout.observe(viewLifecycleOwner) {
            workout->workout?.let {
                updateViewUI(workout)
            }
        }
        return root
    }

    private fun getWorkoutFromUI(): Workout? {
        var workout: Workout?
        workout = activeWorkoutViewModel.curWorkout.value
        if (workout != null) {
            workout.sets = editSets.text.toString().toInt()
        }
        return workout
    }


    fun updateViewUI(workout: Workout) {
        editTitle.setText(workout.name)
        editCategory.setText(workout.category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}