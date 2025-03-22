package com.example.hotel_mobile.View.Pages

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hotel_mobile.ViewModle.AuthViewModle
import com.example.hotel_mobile.ViewModle.HomeViewModle

@Composable
fun HomePage(
    nav: NavHostController,
    homeViewModel: HomeViewModle = hiltViewModel()
) {

    LaunchedEffect(true) {
        homeViewModel.getRooms(1)
    }

    Scaffold {
        it.calculateTopPadding()
        it.calculateBottomPadding()
        Text("home")
    }
}