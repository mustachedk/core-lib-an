package dk.mustache.corelibexample.validation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dk.mustache.corelibexample.databinding.FragmentValidationBinding

class ValidationFragment: Fragment() {
    private lateinit var binding: FragmentValidationBinding
    private val viewModel: ValidationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationBinding.inflate(inflater)

        binding.btnConfirmValidation.setOnClickListener {
            val reply = viewModel.doTheThing()
            Toast.makeText(requireContext(), "Values are: $reply", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
    }
}