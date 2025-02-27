package com.example.hotel_mobile.ViewModle

import androidx.lifecycle.ViewModel
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Data.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.hotel_mobile.Data.room.authintication.AuthinticationDAO
import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.SingUpDto
import com.example.hotel_mobile.Modle.AuthModle
import com.example.hotel_mobile.Modle.NetworkCallHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModle @Inject constructor(
    val authRepository: AuthRepository,
    val authDao: AuthinticationDAO
) : ViewModel() {

    val statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)
    val errorMessage = MutableStateFlow<String?>(null)

    fun loginUser(userDto: LoginDto) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch {
            delay(1000L)
            when (val result = authRepository.loginUser(userDto)) {
                is NetworkCallHandler.Successful<*> -> {
                    var authData = result.data as AuthResultDto
                    authDao.insertAll(AuthModle(null, authData.accessToken, authData.refreshToken))
                    statusChange.update { enNetworkStatus.Complate }
                }
                is NetworkCallHandler.Error -> {
                    errorMessage.update { result.data }
                }
                else->{
                    errorMessage.update { "unexpected Stat" }
                }
            }
        }
    }

    fun signUpUser(userDto: SingUpDto) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch {
            delay(1000L)
            when (val result = authRepository.createNewUser(userDto)) {
                is NetworkCallHandler.Successful<*> -> {
                    var authData = result.data as AuthResultDto
                    authDao.insertAll(AuthModle(null, authData.accessToken, authData.refreshToken))
                    statusChange.update { enNetworkStatus.Complate }
                }
                is NetworkCallHandler.Error -> {
                    errorMessage.update { result.data }
                }
                else->{
                    errorMessage.update { "unexpected Stat" }
                }
            }
        }
    }


}