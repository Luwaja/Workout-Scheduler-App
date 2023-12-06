package edu.uark.workoutreminderapp.ui.addworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.WorkoutApplication
import edu.uark.workoutreminderapp.databinding.FragmentAddworkoutBinding
import androidx.fragment.app.viewModels
import edu.uark.workoutreminderapp.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

const val EXTRA_ID: String = "edu.uark.workoutreminderapp.AddWorkoutFragment.EXTRA_ID"
class AddWorkoutFragment : Fragment() {
    private var _binding: FragmentAddworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val addWorkoutViewModel: AddWorkoutViewModel by viewModels {
        AddWorkoutViewModelFactory((requireActivity().application as WorkoutApplication).repository, -1)
    }

    private lateinit var editTitle: EditText
    private lateinit var editDesc: EditText
    private lateinit var editCategory: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val addWorkoutViewModel =
//            ViewModelProvider(this).get(AddWorkoutViewModel::class.java)

        _binding = FragmentAddworkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editTitle = binding.editTitle
        editDesc = binding.editDescription
        editCategory = binding.editCategory

        val args = arguments
        var id = args?.getInt("ID")
        if (id == null) {
            id = -1
        }

        if (id != -1) {
            addWorkoutViewModel.updateId(id)
        }

        addWorkoutViewModel.curWorkout.observe(viewLifecycleOwner) {
            workout->workout?.let {
                updateViewUI(workout)
            }
        }

        binding.buttonSave.setOnClickListener {
            CoroutineScope(SupervisorJob()).launch {
                if (id==-1) {
                    getWorkoutFromUI()?.let { workout -> addWorkoutViewModel.insert(workout)}
                }
            }
        }




        val textView: TextView = binding.textAddworkout
        addWorkoutViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    fun getWorkoutFromUI(): Workout? {
        val id = -1 // TODO: get workout id passed in here.
        var workout: Workout?
        if (id != -1) {
            workout = addWorkoutViewModel.curWorkout.value
        } else {
            workout = Workout(null, "", "", "", false, 0, 0, 0)
        }
        if (workout != null) {
            workout.name = editTitle.text.toString()
            workout.description = editDesc.text.toString()
            workout.category = editCategory.text.toString()
        }
        return workout
    }

    fun updateViewUI(workout: Workout) {
        editTitle.setText(workout.name)
        editDesc.setText(workout.description)
        editCategory.setText(workout.category)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddWorkoutFragment", "onDestroyView")
        _binding = null
    }
}