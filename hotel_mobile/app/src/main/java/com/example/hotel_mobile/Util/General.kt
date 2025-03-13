package com.example.hotel_mobile.Util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import java.util.Date

object General {


    val BASED_URL = "http://10.0.2.2:5266/api"
   // val BASED_URL = "http://10.0.2.2:7224/api"
    fun String.toDate(): Date {
        var date = Date()
        val stringDateToList = this.split("/")
        val year = stringDateToList[2].toInt()
        val month = stringDateToList[1].toInt()
        val day = stringDateToList[0].toInt()

        date.date = day
        date.month = month
        date.year = year

        return date
    }
}