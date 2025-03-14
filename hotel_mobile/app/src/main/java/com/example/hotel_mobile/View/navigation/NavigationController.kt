package com.example.hotel_mobile.View.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hotel_mobile.View.Pages.LoginPage
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.Pages.HomePage
import com.example.hotel_mobile.View.Pages.SignUpPage
import kotlinx.serialization.Serializable




@Composable
fun NavController(navController: NavHostController)
{
    val authData = General.authData.collectAsState()

    NavHost(
        navController = navController,
        startDestination =if(authData.value!=null)
            Screens.home
            else  Screens.authGraph
    ){

        navigation<Screens.authGraph>(

            startDestination = Screens.login
        ){

           composable<Screens.login>{
               LoginPage(navController)
           }

            composable<Screens.signUp> {
                SignUpPage(navController)
            }

        }

        navigation<Screens.homeGraph>(
            startDestination = Screens.home
        ){
          composable<Screens.home>{
              HomePage(navController)
          }
        }
    }

}