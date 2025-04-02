package com.example.hotel_mobile.View.component

import androidx.compose.runtime.Composable
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.ViewModle.HomeViewModle
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotel_mobile.ViewModle.AuthViewModle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomErrorSnackBar(
    authViewModel: AuthViewModle?=null,
    homeViewModel: HomeViewModle?=null,
    page: @Composable() () -> Unit
){

    val coroutine = rememberCoroutineScope()
    val error = (if(homeViewModel==null) authViewModel!!.errorMessage
            else homeViewModel.errorMessage).collectAsState()
    val isVisible = remember { MutableTransitionState(false) }


    LaunchedEffect(error.value) {
        if (error.value?.isNotEmpty() == true) {
            isVisible.targetState = true;
        }
    }

    LaunchedEffect(error.value) {
        if (error.value?.isNotEmpty() == true) {
            coroutine.launch {
                delay(5000)
                isVisible.targetState = false;
                delay(50)
               // viewModel.clearErrorMessage()
                (when(homeViewModel==null){true->authViewModel!!.clearErrorMessage()
                    else->homeViewModel.clearErrorMessage()})
            }
        }
    }



    val colors = listOf(
        Color.Gray,
        Color.Red,
        Color.Green,
        Color.White
    )


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { it ->
        it.calculateTopPadding()
        it.calculateBottomPadding()
        page()

        AnimatedVisibility(
            isVisible,

            modifier = Modifier

        ) {
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .height(50.dp)
                    .fillMaxWidth()


            ) {


                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp)
                        .background(
                            color = colors[1],
                            shape = RoundedCornerShape(7.dp)
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(colors[3]),
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    Text(
                        error.value.toString(),
                        color = colors[3],
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                        // .background(Color.Red)
                    )

                }
            }
        }


    }

}