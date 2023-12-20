package com.atos.msafe.core.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.atos.msafe.model.File
import com.atos.msafe.model.ItemFile
import com.atos.msafe.model.User
import com.atos.msafe.repository.FirebaseRepository
import kotlinx.coroutines.launch


class CoreViewModel(application: Application) : AndroidViewModel(application) {

    private var firebaseRepository = FirebaseRepository()

    var result: LiveData<Boolean> = firebaseRepository.result
    var errorMessage: LiveData<String> = firebaseRepository.errorMessage
    var accountUser: LiveData<User> = firebaseRepository.accountUser
    var dataListViewer: LiveData<List<ItemFile>> = firebaseRepository.dataListViewer

    fun uploadFileToCloudAddToUser(
        content: ContentResolver,
        file: java.io.File,
        hidden: Boolean,
        view: View?,
        context: Context?
    ) {
        viewModelScope.launch {
            firebaseRepository.uploadFileToCloudAddToUser(
                content,
                file.nameWithoutExtension,
                "." + file.extension,
                file.toUri(),
                hidden,
                view,
                context
            )
        }
    }

    fun getUserFromDatabase() {
        viewModelScope.launch {
            firebaseRepository.getUserFromDatabase()
        }
    }

    fun getDataForListViewer(files: List<File>, hidden: Boolean, context: Context) {
        viewModelScope.launch {
            firebaseRepository.getDataForListViewer(files, hidden, context)
        }
    }

    fun deleteFile(itemFile: ItemFile) {
        viewModelScope.launch {
            firebaseRepository.deleteFile(itemFile)
        }
    }

    fun downloadFileToCash(itemFile: ItemFile, view: View?, context: Context?) {
        viewModelScope.launch {
            firebaseRepository.downloadFileToCash(itemFile, view, context)
        }
    }

    fun moveFile(itemFile: ItemFile) {
        viewModelScope.launch {
            firebaseRepository.moveFile(itemFile)
        }
    }

    fun downloadFileToDownloadFolder(itemFile: ItemFile, view: View?, context: Context?) {
        viewModelScope.launch {
            firebaseRepository.downloadFileToDownloadFolder(itemFile, view, context)
        }
    }

    fun shareFileToOther(itemFile: ItemFile, view: View?, context: Context?) {
        viewModelScope.launch {
            firebaseRepository.shareFileToOther(itemFile, view, context)
        }
    }

    fun setFavourite(itemFile: ItemFile) {
        viewModelScope.launch {
            firebaseRepository.setFavourite(itemFile)
        }
    }
}