package com.example.hotel_mobile.View.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.hotel_mobile.Modle.response.BookingModel
import com.example.hotel_mobile.R
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.Util.General.toCustomString


@Composable
fun BookingShape(
    bookingData: BookingModel
) {
    val context = LocalContext.current
    val thumbnail = bookingData.room.images?.firstOrNull { it.isThumnail == true }?.path
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .background(
                Color.White,
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 2.dp, vertical = 10.dp)
        .clickable {
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(
                    General.handlingBookingStatusBackgroundColor(bookingData.bookingStatus),
                    RoundedCornerShape(40.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .wrapContentHeight()
            ,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center

        ) {
            Text(bookingData.bookingStatus,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Row{
                Text(
                    "${bookingData.bookingStart.toCustomString()}",
                    color = Color.Black.copy(0.40f)
                    )

               Text("  الى  ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    "${bookingData.bookingEnd.toCustomString()}",
                    color = Color.Black.copy(0.40f)
                )

            }
            Text(
                "${bookingData.totalPrice.toString()}",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

        }



        if (!thumbnail.isNullOrEmpty())
            SubcomposeAsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 9.dp)
                    .height(79.dp)
                    .width(73.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
                ,
                model =
                ImageRequest.Builder(context)
                    .data(thumbnail)
                    .build(),
                contentDescription = "",
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center // Ensures the loader is centered and doesn't expand
                    ) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(54.dp) // Adjust the size here
                        )
                    }
                },
            )
        else
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.photo),
                contentDescription = "",
                modifier = Modifier
                    .height(80.dp)
                    .width(100.dp)
            )

    }

}