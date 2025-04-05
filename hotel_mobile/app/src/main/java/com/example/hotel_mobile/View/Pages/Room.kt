package com.example.hotel_mobile.View.Pages

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.hotel_mobile.Modle.enDropDownType
import com.example.hotel_mobile.View.component.CustomDropDownShape
import com.example.hotel_mobile.View.component.CustomSizer
import com.example.hotel_mobile.View.component.MonthDayWithBookingDialog
import com.example.hotel_mobile.View.component.RoomStateShape
import com.example.hotel_mobile.ViewModle.HomeViewModle
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.Calendar
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomPage(
    roomData: RoomDto,
    homeViewModle: HomeViewModle
) {

    val calendar = Calendar.getInstance()

    val currentYear = calendar.get(Calendar.YEAR)

    val context = LocalContext.current;
    val images = roomData.images?.filter { result -> result.isThumnail == false }

    val currentImageIndex = remember { mutableStateOf(0) }
    val swippingDirection = remember { mutableStateOf(0f) }

    val timer = remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet = remember { mutableStateOf(false) }
    var isShowDialog = remember { mutableStateOf(false) }


    val bookingStartYear = remember { mutableStateOf(currentYear) }
    val bookingStartMonth = remember { mutableStateOf(1) }

    var dropDownType = remember { mutableStateOf(enDropDownType.Year) }

    val visableYears = (currentYear..currentYear + 4).toList()
    val visableMonths = (1..12).toList()


    LaunchedEffect(timer.value) {
        delay(3000L)
        when (currentImageIndex.value + 1 == images!!.size) {
            true -> currentImageIndex.value = 0;
            else -> currentImageIndex.value += 1;
        }
        timer.value++;
    }

    LaunchedEffect(timer.value > 10) {
        timer.value = 0
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
                            showBottomSheet.value = true
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
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            if (images != null)
                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (swippingDirection.value > 0) {
                                        when (currentImageIndex.value < images!!.size - 1) {
                                            true -> {
                                                currentImageIndex.value += 1
                                            }

                                            else -> {}
                                        }
                                    } else if (swippingDirection.value < 0) {
                                        when (currentImageIndex.value != 0) {
                                            true -> {
                                                currentImageIndex.value--
                                            }

                                            else -> {}
                                        }
                                    }
                                }, onHorizontalDrag = { _, draggmen ->
                                    swippingDirection.value = draggmen
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                RoomStateShape(roomData.status ?: 0)
                Text(
                    "حالة الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp), contentAlignment = Alignment.Center

                ) {
                    Text(
                        roomData.roomTypeData?.roomTypeName ?: "", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "نوع الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }



            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp), contentAlignment = Alignment.Center

                ) {
                    Text(
                        roomData.bedNumber.toString(), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "عدد السرائر", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp), contentAlignment = Alignment.Center

                ) {
                    Text(
                        roomData.capacity.toString(), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "سعة الغرفة", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }



            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp), contentAlignment = Alignment.Center

                ) {
                    Text(
                        roomData.user
                            ?.userName ?: "", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "اسم المالك", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }


        }

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false;
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "معلومات الحجز",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CustomSizer(height = 10.dp)


                    CustomDropDownShape(
                        listData = visableYears,
                        selectedValue =bookingStartYear,
                        title = "بداية سنة الحجز",
                        dropDownType = enDropDownType.Year,
                        selectedDropType = dropDownType
                    )

                    CustomSizer(height = 5.dp)

                    CustomDropDownShape(
                        listData = visableMonths,
                        selectedValue =bookingStartMonth,
                        title = "بداية شهر الحجز"
                        ,dropDownType = enDropDownType.Month,
                        selectedDropType = dropDownType
                    )
                    CustomSizer(height = 5.dp)

                    Box(
                        modifier =  Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                Color.Black.copy(0.16f)
                                    , RoundedCornerShape(16.dp)
                            ).clickable {
                                isShowDialog.value = !isShowDialog.value
                            }
                        , contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "بداية يوم الحجز",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }

    }
}