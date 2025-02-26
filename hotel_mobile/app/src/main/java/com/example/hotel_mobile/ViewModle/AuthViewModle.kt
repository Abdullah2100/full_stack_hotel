package com.example.hotel_mobile.ViewModle

import androidx.lifecycle.ViewModel
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModle @Inject constructor(val authRepository:AuthRepository) :ViewModel() {

    val statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)

     fun  loginUser(userDto: LoginDto){
        statusChange.update { enNetworkStatus.Loading }
          viewModelScope.launch {
           authRepository.loginUser(userDto)

          }

    }

}