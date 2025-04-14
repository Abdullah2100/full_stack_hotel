package com.example.hotel_mobile.View.Pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.hotel_mobile.ViewModle.HomeViewModle


@Composable
fun BookingPage(
    homeViewModel:HomeViewModle,
    nav: NavHostController
){
    val roomData = homeViewModel.rooms.collectAsState()

    LaunchedEffect(key1 = Unit) {
        homeViewModel.getRooms(1)
    }



       Text("selcondPage")

}