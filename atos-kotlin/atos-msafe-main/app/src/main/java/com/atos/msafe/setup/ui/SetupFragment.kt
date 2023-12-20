package com.atos.msafe.setup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.atos.msafe.R
import com.atos.msafe.databinding.FragmentSetupWelcomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SetupFragment : Fragment() {

    private var _binding: FragmentSetupWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSetupWelcomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSetupLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SetupFragment_to_loginFragment)
        }

        binding.btSetupSignup.setOnClickListener {
            findNavController().navigate(R.id.action_SetupFragment_to_SingupFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}