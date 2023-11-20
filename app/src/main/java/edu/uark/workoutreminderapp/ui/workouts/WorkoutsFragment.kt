package edu.uark.workoutreminderapp.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.databinding.FragmentWorkoutsBinding
import androidx.fragment.app.viewModels
import edu.uark.workoutreminderapp.WorkoutApplication

class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val workoutsViewModel: WorkoutsViewModel by viewModels {
        WorkoutsViewModelFactory((requireActivity().application as WorkoutApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val workoutsViewModel =
//            ViewModelProvider(this).get(WorkoutsViewModel::class.java)

        val adapter = WorkoutsAdapter(this::listItemClicked)

        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textWorkouts
        workoutsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // This should go through each workout and create the list item for it usingthe adapter.
        workoutsViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            workouts.let {
                adapter.submitList(it)
            }
        }

        return root
    }

    private fun listItemClicked(id: Int) {
        //TODO: Open the clicked workout screen (add workout) using the passed in ID.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}