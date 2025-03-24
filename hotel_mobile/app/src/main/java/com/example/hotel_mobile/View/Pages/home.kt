package com.example.hotel_mobile.View.Pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.hotel_mobile.R
import com.example.hotel_mobile.ViewModle.AuthViewModle
import com.example.hotel_mobile.ViewModle.HomeViewModle

@Composable
fun HomePage(
    nav: NavHostController,
    homeViewModel: HomeViewModle = hiltViewModel()
) {

    val roomData = homeViewModel.rooms.collectAsState()

    Scaffold {
        it.calculateTopPadding()
        it.calculateBottomPadding()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            when (roomData.value != null && roomData.value!!.size > 0) {
                true -> {}
                false -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = painterResource(R.drawable.no_found_data),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}