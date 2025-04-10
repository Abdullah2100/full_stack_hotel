package com.example.hotel_mobile.Modle

import kotlinx.datetime.LocalDateTime
import java.util.UUID


data class BookingModel(
   var roomId:UUID,
   var startYear:Int,
   var startMonth:Int,
   var startDay:Int,
   var startTime: String,
   var endYear:Int,
   var endMonth:Int,
   var endDay:Int,
   var endTime: String,
)