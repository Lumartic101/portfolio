package com.atos.msafe.core

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.atos.msafe.R
import com.atos.msafe.databinding.ActivityCoreBinding
import com.atos.msafe.model.User
import com.atos.msafe.repository.FirebaseRepository
import com.atos.msafe.setup.SetupActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import java.util.concurrent.Executor


class CoreActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCoreBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private val permissionsRequestCode = 123
    private val firebaseRepository = FirebaseRepository()
    private lateinit var user: User
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var gridVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Firebase needed to logout when user want
        auth = Firebase.auth

        // Setup the navigation
        navController = findNavController(R.id.nav_host_core)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Receive the User data class from the SetupActivity
        user = intent.extras?.get("USER") as User

        //Permissions
        val listOfPermissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.USE_BIOMETRIC
        )
        // If needed ask user for permission
        ManagePermission(this, listOfPermissions, permissionsRequestCode).checkPermissions()

        // Setup the drawer with the users account
        loadDrawer(
            buildString { append(user.firstName).append(" ").append(user.lastName) },
            user.emailAddress,
            user.profilePicture,
            user.pin
        )
    }

    // When closing the app, delete all the files in the cache
    override fun onDestroy() {
        super.onDestroy()
        applicationContext.cacheDir.deleteRecursively()
        Toast.makeText(baseContext, "onDestroy", Toast.LENGTH_SHORT).show()
    }

    private fun loadDrawer(
        nameUser: String?,
        emailUser: String?,
        profilePicture: String?,
        pinUser: String?
    ) {
        // Create a profile for the user
        val profileUser = ProfileDrawerItem().apply {
            nameText = nameUser ?: ""
            descriptionText = emailUser ?: ""
            iconRes = R.drawable.ic_baseline_account_circle
            identifier = 101
        }
        // Create the AccountHeader
        AccountHeaderView(this, compact = false).apply {
            attachToSliderView(binding.sliderCore)
            addProfiles(profileUser)
            onAccountHeaderListener = { _, profile, current ->
                if (profile is IDrawerItem<*>) {
                    println(current)
                    println(profile.name?.textString.toString())
                    navController.navigate(R.id.accountFragment)
                }
                false
            }
        }
        // Add items to the drawer
        binding.sliderCore.apply {
            itemAdapter.add(
                PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_home
                    iconRes = R.drawable.ic_baseline_home
                    identifier = 1
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_logout
                    iconRes = R.drawable.ic_baseline_logout
                    identifier = 2
                },
                        PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_settings
                    iconRes = R.drawable.ic_baseline_settings
                    identifier = 3
                }
            )
            addStickyDrawerItems(
                SecondaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_about
                    descriptionRes = R.string.txt_drawer_item_about_description
                    iconRes = R.drawable.ic_baseline_question_mark
                    identifier = 10
                }
            )
        }
        binding.sliderCore.setSelection(1)
        // Callback long press
        binding.sliderCore.onDrawerItemLongClickListener = { _, _, position ->
            when (position) {
                1 -> {
                    hiddenGridView()
                }
            }
            false
        }

        // specify a click listener
        binding.sliderCore.onDrawerItemClickListener = { _, _, position ->
            when (position) {
                1 -> {
                    navController.navigate(
                        R.id.homeFragment
                    )
                }
                2 -> {
                    toggleGridHidden(false)
                    // sign the current user out and direct back to setup
                    auth.signOut()
                    intent = Intent(this@CoreActivity, SetupActivity::class.java)
                    if (intent != null) {
                        this@CoreActivity.startActivity(intent)
                        finish()
                    }
                }
                3 -> {
                    toggleGridHidden(false)
                    navController.navigate(R.id.accountFragment)
                }
            }

            false
        }
    }
    // Show or hide the keypad
    private fun toggleGridHidden(state: Boolean) {
        if(state) {
            binding.tableLayout.bringToFront()
            binding.tableLayout2.bringToFront()
            binding.tableLayout3.bringToFront()
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.VISIBLE
            binding.tableLayout3.visibility = View.VISIBLE
            gridVisible = true
        } else {
            binding.tableLayout.visibility = View.GONE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.GONE
            gridVisible = false
        }
    }

    // make the keypad transparent
    private fun hiddenGridViewButClickAble() {
        for (i in 1..9) {
            val buttonId = "hiddenButton$i"
            val logic =
                findViewById<Button>(resources.getIdentifier(buttonId, "id", packageName))
            logic.setBackgroundColor(Color.parseColor("#00000000"))
            logic.elevation = 0f
        }
        gridVisible = true

    }

    // Check the input of the user, and set the pin code
    private fun hiddenGridView(){
        var pin = ""
        if(user.pin.isNullOrEmpty()) {
            binding.root.closeDrawer(binding.sliderCore)
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.txt_dialog_setup_pin)
                .setIcon(R.drawable.ic_baseline_lock)
                .setMessage(R.string.txt_dialog_setup_pin_explain)
                .setNegativeButton(R.string.txt_dialog_button_cancel) { dialog, _ ->
                    dialog.dismiss()
                    toggleGridHidden(false)
                }.setPositiveButton(R.string.txt_dialog_button_oke) { dialog, _ ->
                    dialog.dismiss()
                    toggleGridHidden(true)
                }.show()
        }else{
            toggleGridHidden(true)
            hiddenGridViewButClickAble()
        }
        for (i in 1..9) {
            val buttonId = "hiddenButton$i"
            val logic =
                findViewById<Button>(resources.getIdentifier(buttonId, "id", packageName))
            logic.setOnClickListener {
                if (pin.length > 3) {
                    Toast.makeText(
                        this@CoreActivity,
                        "Try again digits is more then 4",
                        Toast.LENGTH_SHORT
                    ).show()
                    pin = ""
                } else {
                    pin += "$i"

                    if (pin.length == 4) {
                        if (user.pin == pin) {
                            biometricAuthentication()
                            toggleGridHidden(false)
                        } else if (user.pin.isNullOrEmpty()) {
                            user.pin = pin
                            toggleGridHidden(false)

                            MaterialAlertDialogBuilder(this)
                                .setTitle("Password reminder")
                                .setIcon(R.drawable.ic_baseline_lock)
                                .setMessage("Your pincode is: $pin")
                                .setNegativeButton(R.string.txt_dialog_button_cancel) { dialog, _ ->
                                    dialog.dismiss()
                                    toggleGridHidden(true)
                                    user.pin = ""
                                    pin = ""
                                }.setPositiveButton(R.string.txt_dialog_button_oke) { dialog, _ ->
                                    dialog.dismiss()
                                    toggleGridHidden(false)
                                    firebaseRepository.setPinCode(pin)
                                    biometricAuthentication()
                                }.show()
                        }
                    }
                }

            }
        }

    }

    private fun checkBiometricAvailable(): Int {
        return BiometricManager.BIOMETRIC_SUCCESS
    }

    // Prompt for biometric authentication
    private fun biometricAuthentication() {

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    navController.navigate(R.id.action_homeFragment_to_savedFragment)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.txt_prompt_biometric_login))
            .setSubtitle(getString(R.string.txt_prompt_biometric_login_explain))
            .setDeviceCredentialAllowed(true)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    //handle the back press :D close the drawer first and if the drawer is closed close the activity
    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(binding.sliderCore)) {
            binding.root.closeDrawer(binding.sliderCore)
        } else {
            if (gridVisible) {
                toggleGridHidden(false)
            } else {
                super.onBackPressed()
            }
        }
    }
}