package com.example.hotel_mobile.ViewModle

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Data.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
 import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.SingUpDto
 import com.example.hotel_mobile.Modle.NetworkCallHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModle @Inject constructor(
    val authRepository: AuthRepository,

) : ViewModel() {

    val statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)
    val errorMessage = MutableStateFlow<String?>(null)
    val errorHandling = CoroutineExceptionHandler{_,ex->
        Log.d("AuthError",ex.message?:"")
    }

    fun loginUser(userDto: LoginDto) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch(Dispatchers.Main + errorHandling) {
            delay(1000L)
            when (val result = authRepository.loginUser(userDto)) {
                is NetworkCallHandler.Successful<*> -> {
                    var authData = result.data as AuthResultDto
                     statusChange.update { enNetworkStatus.Complate }
                }
                is NetworkCallHandler.Error -> {
                    throw Exception(result.data) ;
                }
                else->{
                    throw Exception("unexpected Stat") ;

                }
            }
        }
    }

    fun signUpUser(userDto: SingUpDto) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch(Dispatchers.Main + errorHandling) {
            delay(1000L)
            when (val result = authRepository.createNewUser(userDto)) {
                is NetworkCallHandler.Successful<*> -> {
                    var authData = result.data as AuthResultDto
                     statusChange.update { enNetworkStatus.Complate }
                }
                is NetworkCallHandler.Error -> {
                    throw Exception(result.data) ;
                }
                else->{
                    throw Exception("unexpected Stat") ;

                }
            }
        }
    }


}