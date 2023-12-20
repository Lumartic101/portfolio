package com.atos.msafe.setup.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.atos.msafe.R
import com.atos.msafe.databinding.FragmentSetupSignupBinding
import com.atos.msafe.model.NewUser
import com.atos.msafe.setup.viewmodel.SignupViewModel
import com.atos.msafe.shared.toggleButton
import java.util.*

class SignupFragment : Fragment() {

    private var _binding: FragmentSetupSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by activityViewModels()
    private var emailPresent: Boolean = false
    private var passwordPresent: Boolean = false
    private var passwordOne: String = "";
    private var passwordTwo: String = "";
    private var passwordRepeatPresent: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btsignup.toggleButton(false)
        viewModel.reset()

        binding.etSignupEmailUser.doOnTextChanged { text, _, _, _ ->
            emailPresent = text.toString().isNotEmpty()
            if(text.toString().contains("@")) {
                enableLogin()
            }else{
                binding.etSignupEmailUser.error = getString(R.string.txt_email_incorrect)
            }
        }

        binding.etSignupPasswordUser.doOnTextChanged { text, _, _, _ ->
            passwordPresent = text.toString().trim().isNotEmpty()
            passwordOne = text.toString();

            if (passwordOne == passwordTwo){
                binding.etSignupPasswordUserRepeatFirst.error = null
                binding.etSignupPasswordUser.error = null

                enableLogin()
            }else{
                binding.etSignupPasswordUserRepeatFirst.error = null
                binding.etSignupPasswordUser.error = null

                binding.etSignupPasswordUser.error = getString(R.string.txt_passwords_incorrect)
                binding.etSignupPasswordUserRepeatFirst.error = getString(R.string.txt_passwords_incorrect)
            }

        }

        binding.etSignupPasswordUserRepeatFirst.doOnTextChanged { text, _, _, _ ->
            passwordRepeatPresent = text.toString().trim().isNotEmpty()
            passwordTwo = text.toString()
            if (passwordOne == passwordTwo){
                enableLogin()
            }else{
                binding.etSignupPasswordUser.error = getString(R.string.txt_passwords_incorrect)
                binding.etSignupPasswordUserRepeatFirst.error = getString(R.string.txt_passwords_incorrect)
            }
        }

        binding.btsignup.setOnClickListener {
            // TODO Check the password, are they the same?, size > 8?
            binding.btsignup.toggleButton(false)
            binding.btsignup.text = getString(R.string.txt_bt_loading)

            viewModel.createUser(NewUser(emailAddress = binding.etSignupEmailUser.text.toString(),
                                        password = binding.etSignupPasswordUser.text.toString()))
        }
        observer()
    }

    private fun enableLogin() {
        if (emailPresent and passwordPresent and passwordRepeatPresent){
            binding.btsignup.toggleButton(true)
        } else {
            binding.btsignup.toggleButton(false)
        }
    }

    private fun observer() {
        viewModel.result.observe(viewLifecycleOwner){
            if (it != null) {
                if (it){
                    Log.i("observer", "New account made!")
                    showDialogAccountMade()
                } else {
                    Log.i("observer", "Could not make account")
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            if (it != null) {
                binding.btsignup.toggleButton(true)
                binding.btsignup.text = getString(R.string.txt_bt_signup_email)
                showDialogProblem(it)
            }
        }
    }

    private fun showDialogAccountMade() {
        val alertDialog = AlertDialog.Builder(this@SignupFragment.activity)

        alertDialog.apply {
            setIcon(R.drawable.ic_baseline_done_all)
            setTitle(getString(R.string.txt_dialog_account_made))
            setMessage(getString(R.string.txt_dialog_account_made_explain))
            setPositiveButton("Oke") { _, _ ->
                viewModel.signOut()
                findNavController().navigate(R.id.SetupFragment)
            }
        }.create().show()
    }

    private fun showDialogProblem(error: String) {
        val alertDialog = AlertDialog.Builder(this@SignupFragment.activity)

        alertDialog.apply {
            setIcon(R.drawable.ic_baseline_error)
            setTitle(getString(R.string.txt_dialog_problem_create_account))
            setMessage(getString(R.string.txt_dialog_problem_explain,
                error.lowercase(Locale.getDefault())
            ))
            setPositiveButton("Oke") { _, _ ->
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}