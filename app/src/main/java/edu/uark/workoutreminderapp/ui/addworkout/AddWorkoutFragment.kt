package edu.uark.workoutreminderapp.ui.addworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uark.workoutreminderapp.databinding.FragmentAddworkoutBinding

class AddWorkoutFragment : Fragment() {
    private var _binding: FragmentAddworkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}