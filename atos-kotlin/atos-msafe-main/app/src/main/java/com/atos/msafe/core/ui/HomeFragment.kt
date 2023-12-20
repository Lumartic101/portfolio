package com.atos.msafe.core.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atos.msafe.R
import com.atos.msafe.core.adapter.FileAdapter
import com.atos.msafe.core.adapter.ItemClickListener
import com.atos.msafe.core.adapter.ItemOptionClickListener
import com.atos.msafe.core.viewmodel.CoreViewModel
import com.atos.msafe.databinding.FragmentCoreHomeBinding
import com.atos.msafe.model.ItemFile
import com.atos.msafe.model.User
import com.atos.msafe.shared.getFileName
import com.atos.msafe.shared.makeCachedFileFromUri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class HomeFragment : Fragment(), ItemClickListener, ItemOptionClickListener {

    private var _binding: FragmentCoreHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoreViewModel by activityViewModels()
    private lateinit var user: User

    // Variables used to store the File we received from the ShareSheet
    private lateinit var receivedUri: Uri
    private lateinit var receivedFile: File
    private lateinit var file: File

    // Variables for the adapter
    private var itemsAdapter = arrayListOf<ItemFile>()
    private var copyList = itemsAdapter.clone() as ArrayList<ItemFile>
    private var fileAdapter = FileAdapter(itemsAdapter, this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCoreHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the Title of the activity
        activity?.title = getString(R.string.txt_home_fragment_label)

        viewModel.getUserFromDatabase()

        // Here we get the intent from the SetupActivity
        if (activity?.intent?.extras?.get("SHARE_SHEET") != null) {
            receivedUri = activity?.intent?.extras?.get("SHARE_SHEET") as Uri
            makeCachedFileFromUri()
            // Then prompt the user where they want to save the file
            showDialog(
                R.drawable.ic_baseline_upload,
                R.string.txt_dialog_upload_file,
                getString(R.string.txt_dialog_upload_file_explain),
                "Hidden folder",
                "normal folder"
            )
        }
        // Callback to select a file to upload
        binding.fabUploadFile.setOnClickListener {
            openFileSelector()
        }

        // Callback to apply a search to the list of items in the view
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            val filteredList = arrayListOf<ItemFile>()
            itemsAdapter.clear()
            if (!text.isNullOrBlank()) {
                val filterPattern = text.toString().lowercase(Locale.getDefault())
                for (item in copyList) {
                    if (item.name.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
                itemsAdapter.addAll(filteredList)
            } else {
                itemsAdapter.addAll(copyList)
            }
            fileAdapter.notifyDataSetChanged()
        }

        binding.etLayout.setEndIconOnClickListener {
            binding.etSearch.text?.clear()
            binding.etSearch.clearFocus()
        }

        initViews()
        observer()
    }

    // Copy a file to the cache directory, then use that to upload the file to the cloud
    private fun makeCachedFileFromUri() {
        receivedFile = File(context?.getFileName(receivedUri).toString())
        val parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(receivedUri, "r", null)
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        file = File(activity?.cacheDir, receivedFile.name)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        // New results, of the account of the user
        viewModel.accountUser.observe(viewLifecycleOwner) { user ->
            this.user = user
            binding.pbLoading.visibility = View.VISIBLE
            this.user.files?.let { list ->
                context?.let { viewModel.getDataForListViewer(list, false, it) }
            }
        }
        // Update list in the viewer
        viewModel.dataListViewer.observe(viewLifecycleOwner) { listOfItemsAdapter ->
            itemsAdapter.clear()
            itemsAdapter.addAll(listOfItemsAdapter)
            copyList = itemsAdapter.clone() as ArrayList<ItemFile>
            binding.pbLoading.visibility = View.INVISIBLE
            fileAdapter.notifyDataSetChanged()
        }
    }
    // Setup the recyclerViewer
    private fun initViews() {
        binding.rvFiles.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvFiles.adapter = fileAdapter
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
                        viewModel.uploadFileToCloudAddToUser(
                            requireActivity().contentResolver,
                            it,
                            false,
                            view,
                            context
                        )
                    }
                }
            }
        }

    // Ask the user where they want to store the incoming file
    private fun showDialog(
        icon: Int,
        @StringRes title: Int,
        faultString: String,
        textPositive: String? = null,
        textNegative: String? = null
    ) {
        val alertDialog = AlertDialog.Builder(this@HomeFragment.activity)

        alertDialog.apply {
            setIcon(icon)
            setTitle(getString(title))
            setMessage(faultString)
            if (textPositive != null) {
                setPositiveButton(textPositive) { _, _ ->
                    handleDialogResponse(true)
                }
            } else {
                setPositiveButton(R.string.txt_dialog_button_oke) { _, _ -> }
            }

            if (textNegative != null) {
                setNegativeButton(textNegative) { _, _ ->
                    handleDialogResponse(false)
                }
            }
            setNeutralButton(R.string.txt_dialog_button_cancel_upload) { _, _ -> }
        }.create().show()
    }

    // Handle the response of the dialog
    private fun handleDialogResponse(b: Boolean) {
        viewModel.uploadFileToCloudAddToUser(
            requireActivity().contentResolver,
            file,
            hidden = b,
            view,
            context,
        )
    }

    // Here we handle the onClick of a element in the adapter
    override fun onClickItem(itemFile: ItemFile) {
        viewModel.downloadFileToCash(itemFile, view, context)
    }

    // Handle onClick of the pop-up menu
    override fun onClickOption(itemFile: ItemFile, itemView: View){
        val popupMenu = PopupMenu(activity, itemView)
        popupMenu.menu.add(R.string.txt_popup_download)
        if (itemFile.favourite) {
            popupMenu.menu.add(R.string.txt_popup_unset_favourite)
        } else {
            popupMenu.menu.add(R.string.txt_popup_set_favourite)
        }

        popupMenu.menu.add(R.string.txt_popup_share)
        popupMenu.menu.add(R.string.txt_popup_move)
        popupMenu.menu.add(R.string.txt_popup_delete)

        popupMenu.setOnMenuItemClickListener {
            when (it.toString()) {
                getString(R.string.txt_popup_download) -> {
                    viewModel.downloadFileToDownloadFolder(itemFile, view, context)
                }
                getString(R.string.txt_popup_set_favourite), getString(R.string.txt_popup_unset_favourite) -> {
                    viewModel.setFavourite(itemFile)
                }
                getString(R.string.txt_popup_share) -> {
                    viewModel.shareFileToOther(itemFile, view, context)
                }
                getString(R.string.txt_popup_move) -> {
                    viewModel.moveFile(itemFile)
                }
                getString(R.string.txt_popup_delete) -> {
                    viewModel.deleteFile(itemFile)
                }
            }
            false
        }
        popupMenu.show()
    }
}