package com.example.hotel_mobile.View.Pages

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.DatePicker
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.timepicker.TimePicker
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.BookingModel
import com.example.hotel_mobile.Modle.enDropDownDateType
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.component.CustomErrorSnackBar
import com.example.hotel_mobile.View.component.CustomSizer
import com.example.hotel_mobile.View.component.RoomStateShape
import com.example.hotel_mobile.ViewModle.HomeViewModle
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomPage(
    roomData: RoomDto,
    homeViewModle:   HomeViewModle
) {




val isLoading = homeViewModle.statusChange.collectAsState();
    val context = LocalContext.current;
    val images = roomData.images?.filter { result -> result.isThumnail == false }

    val currentImageIndex = remember { mutableStateOf(0) }
    val swippingDirection = remember { mutableStateOf(0f) }

    val timer = remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val isOpenDateDialog = remember { mutableStateOf(false) }
    val isOpenTimeDialog = remember { mutableStateOf(false) }

    val currentYear = General.getCurrentYear()
    val currentMonth = General.getCurrentMonth()
    val currentDay = General.getCurrentStartDayAtMonth()
    val currentTime = General.getCurrentCurrentTime()
    val bookingData = remember {
        mutableStateOf(
            BookingModel(
                startYear = currentYear,
                startMonth = currentMonth,
                startDay = currentDay,
                startTime = currentTime,
                endYear = currentYear,
                endMonth = currentMonth,
                endDay = currentDay,
                endTime = currentTime,
                roomId = roomData.roomId!!
            )
        )
    }

    val dropDownType = remember { mutableStateOf(enDropDownDateType.YearStartBooking) }
    val snackbarHostState = remember { SnackbarHostState() }


    fun handlTheSelectionDialog(day: Int, month: Int, year: Int, hour: Int? = 0, minit: Int? = 0) {
        when (dropDownType.value) {
            enDropDownDateType.YearStartBooking -> {
                Log.d("timeResultData","1")

                bookingData.value.startYear = year
            }

            enDropDownDateType.MonthStartBooking -> {
                Log.d("timeResultData","2")
                bookingData.value.startMonth = month
            }

            enDropDownDateType.DayStartBooking -> {
                Log.d("timeResultData","3")

                bookingData.value.startDay = day
            }

            enDropDownDateType.TimeStartBooking -> {
                Log.d("timeResultData","4")

                bookingData.value.startTime = "$hour:$minit"
            }

            enDropDownDateType.YearEndBooking -> {
                Log.d("timeResultData","5")

                bookingData.value.endYear = year
            }

            enDropDownDateType.MonthEndBooking -> {
                Log.d("timeResultData","6")

                bookingData.value.endMonth = month
            }

            enDropDownDateType.DayEndBooking -> {
                Log.d("timeResultData","7")

                bookingData.value.endDay = day
            }

            enDropDownDateType.TimeEndBooking -> {
                Log.d("timeResultData","8")

                bookingData.value.endTime = "$hour:$minit"
            }
        }
    }

    fun handlMonthForDialog(): Int {
        when (dropDownType.value) {
            enDropDownDateType.YearStartBooking -> {
                return bookingData.value.startMonth
            }

            enDropDownDateType.MonthStartBooking -> {

                return bookingData.value.startMonth
            }

            enDropDownDateType.DayStartBooking -> {
                return bookingData.value.startMonth
            }

            enDropDownDateType.YearEndBooking -> {
                return bookingData.value.endMonth
            }

            enDropDownDateType.MonthEndBooking -> {
                return bookingData.value.endMonth
            }

            enDropDownDateType.DayEndBooking -> {
                return bookingData.value.endMonth
            }

            else -> {
                return -1;
            }
        }
    }


    fun handlYearForDialog(): Int {
        when (dropDownType.value) {
            enDropDownDateType.YearStartBooking -> {
                return bookingData.value.startYear
            }

            enDropDownDateType.MonthStartBooking -> {
                return bookingData.value.startYear
            }

            enDropDownDateType.DayStartBooking -> {
                return bookingData.value.startYear
            }

            enDropDownDateType.YearEndBooking -> {
                return bookingData.value.endYear
            }

            enDropDownDateType.MonthEndBooking -> {
                return bookingData.value.endYear
            }

            enDropDownDateType.DayEndBooking -> {
                return bookingData.value.endYear
            }

            else -> {
                return -1;
            }
        }
    }



    LaunchedEffect(isLoading.value) {
        if(showBottomSheet.value==true&& isLoading.value==enNetworkStatus.Complate){
            showBottomSheet.value = false;
            snackbarHostState.showSnackbar("تم انشاء الحجز بنجاح")

        }
    }

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



    CustomErrorSnackBar(
        authViewModel = null,
        homeViewModel = homeViewModle,
        page = {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
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
                                "معلومات بداية الحجز",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            CustomSizer(height = 10.dp)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()

                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.YearStartBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    bookingData.value.startYear.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "بداية سنة الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )

                            }


                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.MonthStartBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,


                                ) {

                                Text(
                                    General.convertMonthToName(
                                        bookingData.value.startMonth,
                                        bookingData.value.startYear
                                    ),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "بداية شهر الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.DayStartBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    bookingData.value.startDay.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "بداية يوم الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.TimeStartBooking

                                        isOpenTimeDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                Text(
                                    bookingData.value.startTime,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "بداية وقت الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            CustomSizer(height = 20.dp)

                            Text(
                                "معلومات نهاية الحجز",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                            CustomSizer(height = 10.dp)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()

                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.YearEndBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    bookingData.value.endYear.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "نهاية سنة الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )

                            }


                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.MonthEndBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,


                                ) {

                                Text(
                                    General.convertMonthToName(
                                        bookingData.value.endMonth,
                                        bookingData.value.endYear
                                    ),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "نهاية شهر الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.DayEndBooking
                                        isOpenDateDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,

                                horizontalArrangement = Arrangement.SpaceBetween

                            ) {

                                Text(
                                    bookingData.value.endDay.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "نهاية يوم الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            CustomSizer(height = 5.dp)

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        Color.Black.copy(0.16f), RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        dropDownType.value = enDropDownDateType.TimeEndBooking

                                        isOpenTimeDialog.value = true
                                    }
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                Text(
                                    bookingData.value.endTime,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "نهاية وقت الحجز",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button (
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                ,
                                enabled = isLoading.value!=enNetworkStatus.Loading,
                                onClick =  {
                                    homeViewModle.createBooking(bookingData.value)
                                }
                            ) {
                                when (isLoading.value) {
                                    enNetworkStatus.Loading -> {
                                        CircularProgressIndicator(
                                            color = Color.Blue, modifier = Modifier
                                                .offset(y = -3.dp)
                                                .height(25.dp)
                                                .width(25.dp)
                                        )
                                    }

                                    else -> {
                                        Text("انشاء حجز جديد",
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }



                            }
                            if (isOpenTimeDialog.value) {
                                Dialog(onDismissRequest = {
                                    isOpenTimeDialog.value = false
                                }) {
                                    Box(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .background(
                                                Color.White,
                                                RoundedCornerShape(17.dp)
                                            )
                                    ) {
                                        TimePicker(
                                            onTimeSelected = { hour, minit ->
                                                handlTheSelectionDialog(0, 0, 0, hour, minit)
                                                isOpenTimeDialog.value = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (isOpenDateDialog.value)
                                Dialog(onDismissRequest = {
                                    isOpenDateDialog.value = false
                                }) {
                                    Box(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .background(
                                                Color.White,
                                                RoundedCornerShape(17.dp)
                                            )
                                    ) {

                                        DatePicker(
                                            dialogState = isOpenDateDialog,
                                            isMonth = dropDownType.value == enDropDownDateType.MonthEndBooking || dropDownType.value == enDropDownDateType.MonthStartBooking,
                                            isYear = dropDownType.value == enDropDownDateType.YearStartBooking || dropDownType.value == enDropDownDateType.YearEndBooking,
                                            month = handlMonthForDialog(),
                                            year = handlYearForDialog(),
                                            modifier = Modifier.padding(16.dp),
                                            onDateSelected = { year, month, day ->

                                                isOpenDateDialog.value = false
                                                handlTheSelectionDialog(day = day, month = month, year = year)
                                            }
                                        )
                                    }
                                }

                        }
                    }
                }

            }
        })



}