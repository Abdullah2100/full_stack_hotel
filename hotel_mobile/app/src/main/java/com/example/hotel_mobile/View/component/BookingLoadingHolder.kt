package com.example.hotel_mobile.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BookingLoaingHolder(){
    Column(
        modifier = Modifier
            .height(120.dp)
            .padding(horizontal = 20.dp)
    ) {


        Row(
            modifier = Modifier

                .padding(top = 5.dp)
                .fillMaxWidth()
                .wrapContentHeight(),

        ) {
            Column{
               Row {
                   Box(
                       modifier = Modifier
                           .height(40.dp)
                           .width(130.dp)
                           .background(
                               Color.Gray.copy(alpha = 0.16f),
                           )

                   )
                   Box(
                       modifier = Modifier
                           .padding(start = 10.dp)
                           .height(40.dp)
                           .width(120.dp)
                           .background(
                               Color.Gray.copy(alpha = 0.16f),
                           )

                   )
               }

                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .height(40.dp)
                        .fillMaxWidth(0.7f)
                        .background(
                            Color.Gray.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(8.dp)
                        )

                )
            }

            Box (
                modifier = Modifier
                    .padding(start = 5.dp)
                    .height(100.dp)
                    .width(150.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )



        }
    }
}