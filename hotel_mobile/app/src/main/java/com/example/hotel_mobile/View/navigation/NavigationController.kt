package com.example.hotel_mobile.View.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hotel_mobile.View.Pages.LoginPage
import com.example.hotel_mobile.ui.theme.Screens


@Composable
fun NavController(navController: NavHostController)
{
    NavHost(
        navController = navController,
        startDestination = Screens.authGraph
    ){

        navigation<Screens.authGraph>(
            startDestination = Screens.login
        ){

           composable<Screens.login>(){
               LoginPage(navController)
           }

        }

        navigation<Screens.homeGraph>(
            startDestination = Screens.login
        ){

        }
    }

}