package com.example.hotel_mobile.View.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hotel_mobile.Util.General
import java.time.YearMonth
import java.util.Calendar


@Composable
fun MonthDayWithBookingDialog(
    isShownDialog: MutableState<Boolean>,
    month: Int,
    year: Int,
) {

    val yearMonth = YearMonth.of(year, month)

    val firstDay = yearMonth.atDay(1)

    val startDayOfWeek = firstDay.dayOfWeek

    val daysInMonth = yearMonth.lengthOfMonth()


    val startDayAt = General.convertDayToNumber(startDayOfWeek.name)


    val dayesList = listOf(
        "السبت",
        "الجمعة",
        "الخميس",
        "الاربعاء",
        "الثلثاء",
        "الاثنين",
        "الاحد",
    )
    if (isShownDialog.value) { // 2
        AlertDialog( // 3
            onDismissRequest = { // 4
                isShownDialog.value = false
            },
            // 5
            title = { Text(text = "اختر اليوم") },
            text = {
                LazyColumn {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            items(dayesList.size) { day ->
                                Text(
                                    text = dayesList[day],
                                    modifier = Modifier

                                )
                            }
                        }

                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .fillMaxWidth()
                        ) {
                            items((0..7).count()) { dayNumber ->
                                when (dayNumber < startDayAt) {
                                    true -> {
                                        Text(
                                            text = "",
                                        )
                                    }

                                    else -> {
                                        Text(
                                            text = (dayNumber).toString(),
                                        )
                                    }
                                }

                            }
                        }

                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(end = 0.dp)
                                .fillMaxWidth()
                        ) {
                            items((0..6).count()) { day ->
                                Text(
                                    text =((0..6).toList()[day]+7).toString(),
                                )

                            }
                        }

                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(end = 0.dp)
                                .fillMaxWidth()
                        ) {
                            items((0..6).count()) { day ->
                                Text(
                                    text =((0..6).toList()[day]+14).toString(),
                                )

                            }
                        }

                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(end = 0.dp)
                                .fillMaxWidth()
                        ) {
                            items((0..6).count()) { day ->
                                Text(
                                    text =((0..6).toList()[day]+21).toString(),
                                )

                            }
                        }

                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .fillMaxWidth()
                        ) {
                            items((0..7).count()) { dayNumber ->
                                val number = dayNumber+28;
                                when (number > daysInMonth) {
                                    true -> {
                                        Text(
                                            text = "",
                                        )
                                    }

                                    else -> {
                                        Text(
                                            text = (number).toString(),
                                        )
                                    }
                                }

                            }
                        }

                    }



                }
            },
            confirmButton = { // 6
                Button(
                    onClick = {
                        isShownDialog.value = false
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            }
        )
    }
}