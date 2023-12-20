package com.atos.msafe.setup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.atos.msafe.model.NewUser
import com.atos.msafe.repository.FirebaseRepository
import kotlinx.coroutines.launch


class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private var firebaseRepository = FirebaseRepository()

    var result: LiveData<Boolean> = firebaseRepository.result

    val errorMessage: LiveData<String> = firebaseRepository.errorMessage

    fun createUser(newUser: NewUser) {
        viewModelScope.launch { firebaseRepository.createUser(newUser) }
    }

    fun signOut(){
        firebaseRepository.signOut()
    }

    fun reset(){
        firebaseRepository.resetData()
    }
}