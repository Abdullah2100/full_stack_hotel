package com.example.hotel_mobile.View.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSizer(
    height:Dp =0.dp,
    width:Dp=0.dp
){
    Box(
        modifier = Modifier
            .height(height)
            .width(width)
    )
}