package com.atos.msafe.setup.ui

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.atos.msafe.R
import com.atos.msafe.core.CoreActivity
import com.atos.msafe.databinding.FragmentSetupLoginBinding
import com.atos.msafe.model.User
import com.atos.msafe.setup.viewmodel.LoginViewModel
import com.atos.msafe.shared.toast
import com.atos.msafe.shared.toggleButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentSetupLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()
    private var emailPresent: Boolean = false
    private var passwordPresent: Boolean = false
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btLogin.toggleButton(false)
        viewModel.reset()

        // Check if the user has filled in both forms
        binding.etLoginPasswordUser.doOnTextChanged { text, _, _, _ ->
            passwordPresent = text.toString().isNotEmpty()
            enableLoginButton()
        }
        binding.etLoginEmailUser.doOnTextChanged { text, _, _, _ ->
            emailPresent = text.toString().trim().isNotEmpty()
            enableLoginButton()
        }
        // Start login, and disable the button
        binding.btLogin.setOnClickListener {
            if (binding.etLoginEmailUser.text.toString().contains("@")) {
                binding.btLogin.toggleButton(false)
                binding.btLogin.text = getString(R.string.txt_bt_loading)
                viewModel.logUserInWithEmailGetUser(
                    binding.etLoginEmailUser.text.toString().trim(),
                    binding.etLoginPasswordUser.text.toString()
                )
            } else {
                binding.etLoginEmailUser.error = getString(R.string.txt_email_incorrect)
                binding.btLogin.toggleButton(false)
            }

        }

        binding.tvResetPassword.setOnClickListener {
            if (binding.etLoginEmailUser.text?.isEmpty() == true) {
                activity?.toast(R.string.txt_toast_no_email, Toast.LENGTH_LONG)
            } else {
                viewModel.sendResetPasswordLink(binding.etLoginEmailUser.text.toString().trim())
            }
        }

        observer()
    }

    private fun enableLoginButton() {
        if (emailPresent and passwordPresent) {
            binding.btLogin.toggleButton(true)
        } else {
            binding.btLogin.toggleButton(false)
        }
    }

    private fun observer() {
        viewModel.userAccount.observe(viewLifecycleOwner) {
            if (it != null) {
                user = it
                // Update Firebase
                if (!user.verified) {
                    viewModel.setVerifiedEmail()
                }
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {
            if (it != null) {
                val auth = Firebase.auth
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    if (currentUser.isEmailVerified) {
                        launchCoreActivity(user)
                    } else {
                        showDialog(
                            R.drawable.ic_baseline_error,
                            R.string.txt_dialog_problem_login,
                            getString(R.string.txt_dialog_problem_verification_explain)
                        )
                        enableLoginButton()
                        binding.btLogin.text = getString(R.string.txt_bt_login)
                    }
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i(TAG, it.toString())
                enableLoginButton()
                binding.btLogin.text = getString(R.string.txt_bt_login)
                showDialog(
                    R.drawable.ic_baseline_error,
                    R.string.txt_dialog_problem_login,
                    getString(R.string.txt_dialog_problem_explain, it)
                )
            }
        }
        viewModel.resetMailSend.observe(viewLifecycleOwner) {
            if (it != null) {
                showDialog(
                    R.drawable.ic_baseline_done_all,
                    R.string.txt_dialog_reset_password,
                    getString(R.string.txt_dialog_reset_password_explain)
                )
            }
        }
    }

    // When the user logs in, start CoreActivity
    private fun launchCoreActivity(user: User) {
        val intent = Intent(context, CoreActivity::class.java)
        intent.putExtra("USER", user)
        context?.startActivity(intent)
        activity?.finish()
    }

    // Show the user a dialog with what is wrong
    private fun showDialog(icon: Int, @StringRes title: Int, faultString: String) {
        val alertDialog = AlertDialog.Builder(this@LoginFragment.activity)

        alertDialog.apply {
            setIcon(icon)
            setTitle(getString(title))
            setMessage(faultString)
            setPositiveButton("Oke") { _, _ ->
            }
        }.create().show()
    }

}