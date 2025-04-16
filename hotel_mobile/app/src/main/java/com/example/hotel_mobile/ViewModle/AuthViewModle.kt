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
import androidx.navigation.NavHostController
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Di.IoDispatcher
import com.example.hotel_mobile.Di.MainDispatcher
import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.SingUpDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.Util.General
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AuthViewModle @Inject constructor(
    private val authRepository: AuthRepository,
    @MainDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authDao: AuthDao
) : ViewModel() {

    private  val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin = _isLogin.asStateFlow()

    private val _statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)
    val statusChange: StateFlow<enNetworkStatus> = _statusChange.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val errorHandling = CoroutineExceptionHandler { _, ex ->
        viewModelScope.launch {
            _statusChange.emit(enNetworkStatus.Error)
            _errorMessage.update {
                ex.message
            }
        }
    }


    init {
            getAuthData()
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
        else if (userDto.brithDay==null)
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
            _statusChange.emit(enNetworkStatus.None)
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
            _statusChange.emit(enNetworkStatus.None)
            snackbarHostState.showSnackbar(message)
            return false
        }
        return true;
    }


    fun loginUser(
        userDto: LoginDto,
        snackbarHostState: SnackbarHostState,
        navController: NavHostController
    ) {
        viewModelScope.launch(ioDispatcher + errorHandling) {
            _statusChange.emit(enNetworkStatus.Loading)

            val resultValidation = validationInputSign(userDto, snackbarHostState)

            if (resultValidation) {
                delay(1000L)
                when (val result = authRepository.loginUser(userDto)) {
                    is NetworkCallHandler.Successful<*> -> {
                        val authData = decodeFromString<AuthResultDto>(result.data.toString())
                        val authEntityData = AuthModleEntity(
                            token = authData.accessToken,
                            refreshToken = authData.refreshToken
                        )
                        authDao.saveAuthData(
                            authEntityData
                        )
                        General.authData.update { authEntityData }
                        _statusChange.update { enNetworkStatus.Complate }
                        _isLogin.emit(true)
                        navController.navigate(Screens.homeGraph)
                    }

                    is NetworkCallHandler.Error -> {
                        throw Exception(result.data?.replace("\"",""));
                    }

                    else -> {
                        throw Exception("unexpected Stat");

                    }
                }

            }


        }
    }

    fun signUpUser(
        userDto: SingUpDto,
        snackbarHostState: SnackbarHostState,
        navController: NavHostController
    ) {
        viewModelScope.launch(ioDispatcher + errorHandling) {

            _statusChange.emit(enNetworkStatus.Loading)

            val resultValidatoin = validationInputSignUp(userDto, snackbarHostState)
            if (resultValidatoin) {
                delay(1000L)
                when (val result = authRepository.createNewUser(userDto)) {
                    is NetworkCallHandler.Successful<*> -> {
                        val authData = decodeFromString<AuthResultDto>(result.data.toString())
                        val authEntityData = AuthModleEntity(
                            token = authData.accessToken,
                            refreshToken = authData.refreshToken
                        )
                        authDao.saveAuthData(
                            authEntityData
                        )
                        General.authData.update { authEntityData }
                        _statusChange.update { enNetworkStatus.Complate }
                        _isLogin.emit(true)
                        navController.navigate(Screens.homeGraph)
                    }
                    is NetworkCallHandler.Error -> {
                        throw Exception(result.data?.replace("\"",""));
                    }

                    else -> {
                        throw Exception("unexpected Stat");

                    }
                }

            }

        }
    }


     fun getAuthData ()  {
        viewModelScope.launch {
            val result =  authDao.getAuthData();
            General.authData.update {  result };
            _isLogin.emit(result!=null)

        }
    }



    suspend fun clearErrorMessage(){
        _errorMessage.emit("")
    }
}