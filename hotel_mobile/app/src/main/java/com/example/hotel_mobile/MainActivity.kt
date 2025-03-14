package com.example.hotel_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.navigation.NavController
import com.example.hotel_mobile.ViewModle.AuthViewModle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    var keepSplash = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplash
            }
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModle = hiltViewModel<AuthViewModle>()
            val isLogin = authViewModle.isLogin.collectAsState()

            LaunchedEffect(isLogin.value) {
                if (isLogin.value != null) {
                    keepSplash = false
                }
            }

            if (isLogin.value != null)
                NavController(navController, isLogin.value)

        }
    }
}
