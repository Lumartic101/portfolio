package com.atos.msafe.core.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.atos.msafe.R
import com.atos.msafe.core.viewmodel.AccountViewModel
import com.atos.msafe.databinding.FragmentCoreAccountBinding
import com.atos.msafe.shared.makeCachedFileFromUri
import com.atos.msafe.shared.toggleButton

class AccountFragment : Fragment() {

    private var _binding: FragmentCoreAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel
    private var firstPresent: Boolean = false
    private var secondPresent: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoreAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    // Set all callbacks
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        binding.btChange.toggleButton(false)

        binding.etFirstname.doOnTextChanged { text, _, _, _ ->
            firstPresent = text.toString().isNotEmpty()
            enableChange()
        }
        binding.etLastname.doOnTextChanged { text, _, _, _ ->
            secondPresent = text.toString().trim().isNotEmpty()
            enableChange()
        }

        binding.btChange.setOnClickListener {
            binding.btChange.toggleButton(false)
            binding.btChange.text = getString(R.string.txt_bt_nameChangedSuccess)


            viewModel.updateName(
                binding.etFirstname.text.toString(),
                binding.etLastname.text.toString()
            )
            findNavController().navigate(R.id.homeFragment)
        }

        binding.fabUploadFile.setOnClickListener {
            openFileSelector()
        }
    }

    // The user is prompted with their file selector, here they can select any file they want
    private fun openFileSelector() {
        val intentResult = Intent(Intent.ACTION_GET_CONTENT)
        intentResult.type = "*/*"
        Intent.createChooser(intentResult, "Open file")
        getResult.launch(intentResult)
    }

    // When the user has selected a file, we can use the data from it
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri = result.data?.data
                if (fileUri != null) {
                    // Upload to the cloud and add ref to user in FireStore
                    activity?.makeCachedFileFromUri(fileUri)?.let {
                        viewModel.updateProfilePicture(
                            requireActivity().contentResolver,
                            it,
                        )
                    }
                }
            }
        }

    private fun enableChange() {
        if (firstPresent and secondPresent) {
            binding.btChange.toggleButton(true)
        } else {
            binding.btChange.toggleButton(false)
        }
    }

}