package com.example.hotel_mobile.View.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.R
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.Util.MoudelToDto.toRoomDto
import com.example.hotel_mobile.View.component.RoomLoaingHolder
import com.example.hotel_mobile.ViewModle.HomeViewModle
import okhttp3.internal.wait
@Composable
fun RoomShape(
    roomModel: RoomModel,
    nav: NavHostController
){
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(
                Color.White,
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 9.dp, vertical = 10.dp)
            .clickable {
                nav.navigate(Screens.Room(roomModel.toRoomDto()))
            }
    ) {
        SubcomposeAsyncImage(
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model =
            ImageRequest.Builder(context)
                .data(
                   roomModel.images!!.first { img -> img.isThumnail == true }.path
                )
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
        Row(
            modifier = Modifier

                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            RoomStateShape(roomModel.status?:0)

            Box(
                modifier = Modifier


                )
            {
                Text(roomModel.roomTypeModel?.roomTypeName?:"")
            }


        }

    }

}