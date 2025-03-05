package com.example.hotel_mobile.View.Pages

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.hotel_mobile.ViewModle.AuthViewModle
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginPage(
    nav:NavHostController,
    finalScreenViewModel:AuthViewModle = hiltViewModel()
){

    Scaffold {
        it.calculateTopPadding()
        it.calculateBottomPadding()

    }


}