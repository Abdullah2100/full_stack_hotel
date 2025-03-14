package com.example.hotel_mobile.View.Pages

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hotel_mobile.ViewModle.AuthViewModle

@Composable
fun HomePage(
    nav: NavHostController,
    finalScreenViewModel: AuthViewModle = hiltViewModel()
) {

    Scaffold {
        it.calculateTopPadding()
        it.calculateBottomPadding()
        Text("home")
    }
}