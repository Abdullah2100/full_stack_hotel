package com.example.hotel_mobile.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RoomLoaingHolder(){
    Column(
        modifier = Modifier
            .height(290.dp)
            .padding(horizontal = 20.dp)
    ) {
        Box (
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(
                    Color.Gray.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Row(
            modifier = Modifier

                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.5f)
                    .background(
                        Color.Gray.copy(alpha = 0.16f),
                    )

            )

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.4f)
                    .background(
                        Color.Gray.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(8.dp)
                    )

            )
        }
    }
}