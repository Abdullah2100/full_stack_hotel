package com.example.hotel_mobile.View.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.component.RoomStateShape
import com.example.hotel_mobile.ViewModle.HomeViewModle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomPage(
    roomData: RoomDto,
    homeViewModle: HomeViewModle
) {

    val context = LocalContext.current;
    val images = roomData.images?.filter { result -> result.isThumnail == false }

    val currentImageIndex = remember { mutableStateOf(0) }
    val swippingDirection = remember { mutableStateOf(0f) }

    val timer = remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet = remember { mutableStateOf(false) }
    val curoutineScop = rememberCoroutineScope()


    LaunchedEffect (timer.value){
        Log.d("counterIsCalled", "${currentImageIndex.value}")

        delay(3000L)
        when (currentImageIndex.value+1==images!!.size){
            true->currentImageIndex.value=0;
            else -> currentImageIndex.value+=1;
        }
        timer.value++;
    }

    LaunchedEffect (timer.value>10){
        timer.value=0
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            showBottomSheet.value= true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp) // Optional padding
                    ) {
                        Text(text = "حجز", fontSize = 18.sp)
                    }
                }
            }
        }
    ) {
        it.calculateBottomPadding()
        it.calculateTopPadding()

        Column(
            modifier = Modifier.
            padding(horizontal = 10.dp)
        ) {
            if (images != null)
                Box(
                    modifier = Modifier
                        .pointerInput(Unit){
                            detectHorizontalDragGestures(
                                onDragEnd ={
                                    if(swippingDirection.value>0){
                                        when(currentImageIndex.value<images!!.size-1){
                                            true->{currentImageIndex.value+=1}
                                            else->{}
                                        }
                                    }else if (swippingDirection.value<0){
                                        when(currentImageIndex.value!=0){
                                            true->{currentImageIndex.value--}
                                            else->{}
                                        }
                                    }
                                }
                                , onHorizontalDrag = {_,draggmen->
                                    swippingDirection.value= draggmen
                                }
                            )
                        }
                ) {
                    SubcomposeAsyncImage(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 35.dp)
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        model =
                        ImageRequest.Builder(context)
                            .data(images[currentImageIndex.value].path)
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
                }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ,modifier = Modifier.padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                RoomStateShape(roomData.status?:0)
                Text("حالة الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ,modifier = Modifier.padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp)

                    , contentAlignment = Alignment.Center

                ){
                    Text(roomData.roomTypeData?.roomTypeName?:"", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                }
                Text("نوع الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }



            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ,modifier = Modifier.padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp)

                    , contentAlignment = Alignment.Center

                ){
                Text(roomData.bedNumber.toString(), fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                }
                Text("عدد السرائر", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ,modifier = Modifier.padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp)

                    , contentAlignment = Alignment.Center

                ){
                    Text(roomData.capacity.toString(), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                }
                Text("سعة الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }



            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ,modifier = Modifier.padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp)

                    , contentAlignment = Alignment.Center

                ){
                    Text(roomData.user
                        ?.userName?:"", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                }
                Text("اسم المالك", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }


        }

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                         showBottomSheet.value=false;
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Button(onClick = {

                    showBottomSheet.value=false;
                }) {
                    Text("Hide bottom sheet")
                }
            }
        }
    }

}