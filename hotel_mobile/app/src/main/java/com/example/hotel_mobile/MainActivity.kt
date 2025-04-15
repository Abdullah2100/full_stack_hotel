package com.example.hotel_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.navigation.NavController
import com.example.hotel_mobile.ViewModle.AuthViewModle
import dagger.hilt.android.AndroidEntryPoint


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
            MaterialTheme{
                val navController = rememberNavController()
                val authViewModle = hiltViewModel<AuthViewModle>()
                val isLogin = authViewModle.isLogin.collectAsState()
                val selectedScreen = rememberSaveable{
                    mutableStateOf(0)
                }
                val pages = listOf(Screens.home,Screens.booking)

                LaunchedEffect(isLogin.value) {
                    if (isLogin.value != null) {
                        keepSplash = false
                    }
                }


                if (isLogin.value != null){
                    Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    content = {
                                        if(isLogin.value==true){
                                            pages.fastForEachIndexed {index, value ->
                                                NavigationBarItem(selected = selectedScreen.value == index,
                                                    onClick = {
                                                        if(selectedScreen.value!=index)
                                                            navController.navigate(value)
                                                        selectedScreen.value = index
                                                    },
                                                    icon = {
                                                        when (index) {
                                                            0 -> {
                                                                Icon(
                                                                    imageVector = Icons.Default.Home,
                                                                    contentDescription = ""
                                                                )
                                                            }
//                                                            1->{
//                                                                Icon(imageVector = Icons.Default.Settings, contentDescription ="" )
//                                                            }
                                                            else -> {

                                                                Icon(
                                                                    imageVector = Icons.Default.DateRange,
                                                                    contentDescription = ""
                                                                )
                                                            }

                                                        }
                                                    })
                                            }

                                        }
                                      }
                                )
                            }



                    ) {
                        it.calculateTopPadding()
                        it.calculateBottomPadding()
                        NavController(navController, isLogin.value)}

                }

            }


        }
    }
}
