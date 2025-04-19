package com.example.hotel_mobile.Modle.response

import java.time.LocalDateTime
import java.util.UUID


data class BookingModel(
    val  bookingId:UUID,
    val roomId:UUID,
    val  userId:UUID,
    val bookingStart:  LocalDateTime,
    val bookingEnd:  LocalDateTime,
    val bookingStatus:String,
    val totalPrice:Float,
    val servicePayment:Float?,
    val maintenancePayment:Float?,
    val paymentStatus:String?,
    val  createdAt: LocalDateTime,

    val cancelledAt:LocalDateTime?,
    val cancellationReason:String?,

    val actualCheckOut: LocalDateTime?,
    val room: RoomModel,
    val user: UserModel
)