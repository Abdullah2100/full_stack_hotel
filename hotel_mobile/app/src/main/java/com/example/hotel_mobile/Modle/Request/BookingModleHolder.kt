package com.example.hotel_mobile.Modle.Request

import java.util.UUID

data class BookingModleHolder(
    var startYear:Int,
    var startMonth:Int,
    var startDay:Int?,
    var startTime: String?,
    var endYear:Int,
    var endMonth:Int,
    var endDay:Int?,
    var endTime: String?,
    var roomId:UUID?
)
