package edu.uark.workoutreminderapp.ui.addworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.WorkoutApplication
import edu.uark.workoutreminderapp.databinding.FragmentAddworkoutBinding
import androidx.fragment.app.viewModels

const val EXTRA_ID: String = "edu.uark.workoutreminderapp.AddWorkoutFragment.EXTRA_ID"
class AddWorkoutFragment : Fragment() {
    private var _binding: FragmentAddworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val addWorkoutViewModel: AddWorkoutViewModel by viewModels {
        AddWorkoutViewModelFactory((requireActivity().application as WorkoutApplication).repository, -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addWorkoutViewModel =
            ViewModelProvider(this).get(AddWorkoutViewModel::class.java)

        _binding = FragmentAddworkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAddworkout
        addWorkoutViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        addWorkoutViewModel.curWorkout.observe(viewLifecycleOwner) {
            workout->workout?.let {
                // TODO: Add code to set the containers in add workout view to match current workout
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}