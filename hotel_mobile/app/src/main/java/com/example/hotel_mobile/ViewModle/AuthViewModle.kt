package com.example.hotel_mobile.ViewModle

import android.util.Log
import androidx.compose.material3.SnackbarHostState
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
    val errorHandling = CoroutineExceptionHandler { _, ex ->
        Log.d("AuthError", ex.message ?: "")
        viewModelScope.launch {
            statusChange.emit(enNetworkStatus.Error)
            errorMessage.update {
                ex.message
            }
        }
    }

    private suspend fun validationInputSignUp(
        userDto: SingUpDto,
        snackbarHostState: SnackbarHostState
    ): Boolean {
        var message = ""
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        val passwordContainCharCapitaRegex = "^(?=(.*[A-Z].*[A-Z])).*$".toRegex()
        val passwordContainCharSmallRegex = "^(?=(.*[a-z].*[a-z])).*$".toRegex()
        val passwordContainNumberRegex = "^(?=(.*\\d.*\\d)).*$".toRegex()

        val passwordContainSpicialCharecterRegex = """
                   ^(?=(.*[!@#\\${'$'}%^&*()_+|/?<>:;'\\"-].*[!@#\\${'$'}%^&*()_+|/?<>:;'\\"-])).*${'$'}
        """.trimIndent()
            .toRegex()



        if (userDto.name.length < 1) {
            message = "الاسم لا يمكن ان يكون فارغا"
        } else if (userDto.userName.isEmpty())
            message = "اسم المستخدم لا يمكن ان يكون فارغا"
        else if (userDto.email.isEmpty())
            message = "الايميل لا يمكن ان يكون فارغا"
        else if (userDto.phone.length > 10 || userDto.phone.isEmpty())
            message = "رقم الهاتف لا يمكن ان يكون فارغا او اكثر من 10"
        else if (userDto.brithDay.isNullOrEmpty() || userDto.brithDay == "لم يتم اختيار اي تاريخ")
            message = "تاريخ الميلاد لا يمكن ان يكون فارغا"
        else if (userDto.address.isEmpty())
            message = " العنوان لا يمكن ان يكون فارغا"
        else if (userDto.password.isEmpty())
            message = "كلمة المرور لا يمكن ان تكون فارغا"
        else if (userDto.password.isEmpty() || userDto.password.length > 16)
            message = "كلمة المرور لا بد ان لا تكون فارغة او اكثر من 16 حرف"
        else if (!userDto.email.matches(emailRegex))
            message = "لا بد من ادخال ايميل  صالح"
        else if (!userDto.password.matches(passwordContainCharCapitaRegex))
            message = "لا بد ان تحتوي كلمة المرور على حرفين  capital"
        else if (!userDto.password.matches(passwordContainCharSmallRegex))
            message = "لا بد ان تحتوي كلمة المرور على حرفين  small"
        else if (!userDto.password.matches(passwordContainNumberRegex))
            message = "لا بد ان تحتوي كلمة المرور على رقمين"
        else if (!userDto.password.matches(passwordContainSpicialCharecterRegex))
            message = "لا بد ان تحتوي كلمة المرور على رمزين"
        if (message.isNotEmpty()) {
            statusChange.emit(enNetworkStatus.None)
            snackbarHostState.showSnackbar(message)
            return false
        }
        return true;

    }

    private suspend fun validationInputSign(
        userDto: LoginDto,
        snackbarHostState: SnackbarHostState
    ): Boolean {

        var message = ""
        if (userDto.userNameOrEmail.isEmpty()) {
            message = "الاسم المستخدم / الايميل  لا يمكن ان يكون فارغا"
        } else if (userDto.password.isEmpty())
            message = "كلمة المرور لا يمكن ان تكون فارغة"

        if (message.isNotEmpty()) {
            statusChange.emit(enNetworkStatus.None)
            snackbarHostState.showSnackbar(message)
            return false
        }
        return true;
    }


    fun loginUser(userDto: LoginDto, snackbarHostState: SnackbarHostState) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch(Dispatchers.Main + errorHandling) {

            val resultValidation = validationInputSign(userDto, snackbarHostState)
            Log.d("theisResultFromValidation","the validation is ${resultValidation}")
            if (resultValidation) {
                delay(1000L)
                when (val result = authRepository.loginUser(userDto)) {
                    is NetworkCallHandler.Successful<*> -> {
                        var authData = result.data as AuthResultDto
                        statusChange.update { enNetworkStatus.Complate }
                    }

                    is NetworkCallHandler.Error -> {
                        throw Exception(result.data);
                    }

                    else -> {
                        throw Exception("unexpected Stat");

                    }
                }

            }


        }
    }

    fun signUpUser(userDto: SingUpDto, snackbarHostState: SnackbarHostState) {
        statusChange.update { enNetworkStatus.Loading }
        viewModelScope.launch(Dispatchers.Main + errorHandling) {

            val resultValidatoin = validationInputSignUp(userDto, snackbarHostState)
            if (resultValidatoin) {
                delay(1000L)
                when (val result = authRepository.createNewUser(userDto)) {
                    is NetworkCallHandler.Successful<*> -> {
                        var authData = result.data as AuthResultDto
                        statusChange.update { enNetworkStatus.Complate }
                    }

                    is NetworkCallHandler.Error -> {
                        throw Exception(result.data);
                    }

                    else -> {
                        throw Exception("unexpected Stat");

                    }
                }

            }

        }
    }


}