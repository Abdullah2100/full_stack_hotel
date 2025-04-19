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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.hotel_mobile.Modle.response.RoomModel
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.R
import com.example.hotel_mobile.Util.MoudelToDto.toRoomDto

@Composable
fun RoomShape(
    roomModel: RoomModel,
    nav: NavHostController
){
    val context = LocalContext.current
    val isHasThumnail = remember {
        mutableStateOf(roomModel.images?.firstOrNull{it->it.isThumnail==true}!=null)
    }

    Column(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .background(
                Color.White,
                RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 9.dp, vertical = 10.dp)
            .clickable {
                nav.navigate(Screens.Room(roomModel.toRoomDto()))
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isHasThumnail.value)
        SubcomposeAsyncImage(
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model =
            ImageRequest.Builder(context)
                .data(
                   roomModel.images?.firstOrNull { img -> img.isThumnail == true }?.path
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
        else 
            Image(
                painter = painterResource(R.drawable.photo),
                contentDescription =""
            ,
                modifier = Modifier.size(250.dp))

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