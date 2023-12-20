package com.atos.msafe.setup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.atos.msafe.model.User
import com.atos.msafe.repository.FirebaseRepository
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var firebaseRepository = FirebaseRepository()

    var result: LiveData<Boolean> = firebaseRepository.result
    val userAccount: LiveData<User> = firebaseRepository.accountUser
    val errorMessage: LiveData<String> = firebaseRepository.errorMessage
    val resetMailSend: LiveData<Boolean> =firebaseRepository.resetMailSend

    fun logUserInWithEmailGetUser(emailAddress: String, password: String){
        viewModelScope.launch {
            firebaseRepository.logUserInWithEmailGetUser(emailAddress, password)
        }
    }

    fun reset(){
        firebaseRepository.resetData()
    }

     fun setVerifiedEmail() {
         viewModelScope.launch {
             firebaseRepository.setVerifiedEmail()
         }
    }

     fun sendResetPasswordLink(email: String) {
         viewModelScope.launch {
             firebaseRepository.sendResetPasswordLink(email)
         }
    }
}