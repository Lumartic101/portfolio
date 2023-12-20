package com.atos.msafe.setup

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.atos.msafe.R
import com.atos.msafe.core.CoreActivity
import com.atos.msafe.databinding.ActivitySetupBinding
import com.atos.msafe.model.User
import com.atos.msafe.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionRes
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import kotlinx.coroutines.launch

class SetupActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySetupBinding

    private lateinit var auth: FirebaseAuth
    private val firebaseRepository = FirebaseRepository()
    private lateinit var user: User
    private var uriShareSheet: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        //get the DrawerLayout from the Drawer.Result
        binding.sliderSetup.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val navController = findNavController(R.id.nav_host_setup)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                getUriToFile(intent)
            }
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        /**
         * If user is not null and user has verified email, use account, and jump to [CoreActivity]
         * When the email address is not verified we sign out the user and redirect him to the setup.
         */
        if (currentUser == null) {
            goToSetup()
        }
        if (currentUser?.isEmailVerified == false){
            auth.signOut()
            goToSetup()
        }
        // Get lasted data of the user
        lifecycleScope.launch {
            firebaseRepository.getUserFromDatabase()
        }
        observer()
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    private fun getUriToFile(intent: Intent) {
        uriShareSheet = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri
    }

    private fun goToSetup() {
        auth.signOut()
        loadDrawer()
        findNavController(R.id.nav_host_setup).navigate(R.id.action_bootFragment_to_SetupFragment)
    }

    private fun observer() {
        firebaseRepository.accountUser.observe(this) {
            user = it
            // Update Firebase
            if (!user.verified) {
                lifecycleScope.launch {
                    firebaseRepository.setVerifiedEmail()
                }
            }
        }
        firebaseRepository.errorMessage.observe(this) {
            showDialogProblem(R.string.txt_dialog_problem_general, it)
        }
        firebaseRepository.result.observe(this) {
            if (it) {
                // Load view in to the Core app
                launchCoreActivity()
            }
        }
    }

    private fun launchCoreActivity() {
        intent = Intent(this@SetupActivity, CoreActivity::class.java)
        if (uriShareSheet != null) {
            intent.putExtra("SHARE_SHEET", uriShareSheet)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.putExtra("USER", user)
        this@SetupActivity.startActivity(intent)
        finish()
    }

    private fun loadDrawer() {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        val navController = findNavController(R.id.nav_host_setup)
        // get the reference to the slider and add the items
        binding.sliderSetup.apply {
            addItems(
                PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_home
                    descriptionRes = R.string.txt_drawer_item_home_description
                    iconRes = R.drawable.ic_baseline_home
                    identifier = 1
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_login
                    descriptionRes = R.string.txt_drawer_item_login_description
                    iconRes = R.drawable.ic_baseline_login
                    identifier = 2
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.txt_drawer_item_signup
                    descriptionRes = R.string.txt_drawer_item_signup_description
                    iconRes = R.drawable.ic_baseline_edit
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

        // specify a click listener
        binding.sliderSetup.onDrawerItemClickListener = { _, _, position ->
            when (position) {
                0 -> navController.navigate(R.id.SetupFragment)
                1 -> navController.navigate(R.id.loginFragment)
                2 -> navController.navigate(R.id.SingupFragment)

            }
            false
        }
    }

    private fun showDialogProblem(@StringRes title: Int, faultString: String) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setIcon(R.drawable.ic_baseline_error)
            setTitle(getString(title))
            setMessage(faultString)
            setPositiveButton("Oke") { _, _ ->
            }
        }.create().show()
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (binding.root.isDrawerOpen(binding.sliderSetup)) {
            binding.root.closeDrawer(binding.sliderSetup)
        } else {
            finish()
        }
    }
}