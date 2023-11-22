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
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}