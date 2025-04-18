package com.example.hotel_mobile

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hotel_mobile.Modle.ButtonSheetItem
import com.example.hotel_mobile.Modle.Screens
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
            MaterialTheme {
                val navController = rememberNavController()
                val authViewModle = hiltViewModel<AuthViewModle>()
                val isLogin = authViewModle.isLogin.collectAsState()
                val selectedScreen = rememberSaveable {
                    mutableStateOf(0)
                }
                val pages = listOf(
                    Screens.home,
                    Screens.booking,
                    Screens.myRooms,
                    Screens.bookingForMyRoom,
                    Screens.home,
                )
                val buttonNavItem = listOf(
                    ButtonSheetItem(
                        Icons.Default.Home,
                        "الرئيسية"
                    ),
                    ButtonSheetItem(
                        Icons.Default.DateRange,
                        "حجوزاتي"
                    ),
                    ButtonSheetItem(
                        ImageVector.vectorResource(id = R.drawable.bedroom),
                        "غرفي"
                    ),

                    ButtonSheetItem(
                        ImageVector.vectorResource(id = R.drawable.baseline),
                        "الحجوزات"
                    ),

                    ButtonSheetItem(
                        Icons.Default.Settings,
                        "الاعدادات"
                    )
                )

                LaunchedEffect(isLogin.value) {
                    if (isLogin.value != null) {
                        keepSplash = false
                    }
                }


                if (isLogin.value != null) {
                    Scaffold(
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            Log.d("currentDistination", "${navBackStackEntry?.destination?.route}")
                            if (isLogin.value == true &&
                                (
                                        navBackStackEntry?.destination?.hasRoute(Screens.home::class) == true
                                                ||
                                                navBackStackEntry?.destination?.hasRoute(Screens.myRooms::class) == true
                                                ||
                                                navBackStackEntry?.destination?.hasRoute(Screens.bookingForMyRoom::class) == true
                                                ||
                                                navBackStackEntry?.destination?.hasRoute(Screens.booking::class) == true
                                        )
                            )
                                NavigationBar(
                                    content = {
                                        pages.fastForEachIndexed { index, value ->
                                            NavigationBarItem(selected = selectedScreen.value == index,
                                                onClick = {
                                                    if (selectedScreen.value != index)
                                                        navController.navigate(value)
                                                    selectedScreen.value = index
                                                },
                                                label = {
                                                    Text(
                                                        text = buttonNavItem.get(index).name,
                                                        color = if (selectedScreen.value != index) Color.Black.copy(
                                                            0.46f
                                                        )
                                                        else Color.Black
                                                    )
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = buttonNavItem.get(index).icon,
                                                        contentDescription = "",
                                                        tint = if (selectedScreen.value != index) Color.Black.copy(
                                                            0.46f
                                                        )
                                                        else Color.Black
                                                    )
                                                })
                                        }

                                    }
                                )
                        }


                    ) {
                        it.calculateTopPadding()
                        it.calculateBottomPadding()
                        NavController(navController, isLogin.value)
                    }

                }

            }


        }
    }
}
