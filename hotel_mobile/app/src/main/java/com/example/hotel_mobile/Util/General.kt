package com.example.hotel_mobile.Util

import java.util.Date

object General {

    val BASED_URL = "http://192.168.20.138:5266/api"
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