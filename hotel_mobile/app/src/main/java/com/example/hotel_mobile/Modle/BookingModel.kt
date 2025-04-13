package com.example.hotel_mobile.Modle

import kotlinx.datetime.LocalDateTime
import java.util.UUID


data class BookingModel(
   var roomId:UUID?=null,
   var startYear:Int,
   var startMonth:Int,
   var startDay:Int?=null,
   var startTime: String,
   var endYear:Int,
   var endMonth:Int,
   var endDay:Int?=null,
   var endTime: String,
)