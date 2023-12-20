package com.atos.msafe.core.viewmodel

import android.app.Application
import android.content.ContentResolver
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.atos.msafe.repository.FirebaseRepository
import kotlinx.coroutines.launch

class AccountViewModel (application: Application) : AndroidViewModel(application) {

    private var firebaseRepository = FirebaseRepository()
    var result: LiveData<Boolean> = firebaseRepository.result

    fun updateName(firstName: String, lastName: String) {
        viewModelScope.launch { firebaseRepository.updateNameUser(firstName,lastName) }
    }

    fun updateProfilePicture(content: ContentResolver, file: java.io.File) {
        viewModelScope.launch {
            firebaseRepository.updateProfilePic(content, file.extension, file.toUri())
        }
    }}

