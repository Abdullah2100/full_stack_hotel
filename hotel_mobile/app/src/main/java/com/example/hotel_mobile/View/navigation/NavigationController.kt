package com.example.hotel_mobile.View.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavController(navController: NavHostController)
{
    NavHost(
        navController = navController,
        startDestination = "loginComponent"
    ){

    }

}