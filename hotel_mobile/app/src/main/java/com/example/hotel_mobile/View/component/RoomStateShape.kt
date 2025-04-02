package com.example.hotel_mobile.View.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotel_mobile.Util.General


@Composable
fun RoomStateShape(status:Int){
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(150.dp)
            .border(
                width = 1.dp,
                color = General.convertRoomStateToColor(
                    status = status
                ),
                shape = RoundedCornerShape(8.dp)
            )
        , contentAlignment = Alignment.Center

    ){
        Text(
            General
                .convertRoomStateToText(status),
            color =  General.convertRoomStateToColor(
                status
            ))

    }
}