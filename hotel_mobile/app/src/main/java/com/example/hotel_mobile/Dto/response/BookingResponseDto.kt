package com.example.hotel_mobile.Dto.response

import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Dto.UserDto
import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID


@Serializable
data class BookingResponseDto(
    @Serializable(with = UUIDKserialize::class)
    val  bookingId:UUID,
    @Serializable(with = UUIDKserialize::class)
    val roomId:UUID,
    @Serializable(with = UUIDKserialize::class)
    val  userId:UUID,
    @Serializable(with= LocalDateTimeKserialize::class)
     val bookingStart:LocalDateTime,
    @Serializable(with= LocalDateTimeKserialize::class)
    val bookingEnd:LocalDateTime,
    val bookingStatus:String,
    val totalPrice:Float,
    val servicePayment:Float?,
    val maintenancePayment:Float?,
    val paymentStatus:String?,
    @Serializable(with= LocalDateTimeKserialize::class)
    val  createdAt:LocalDateTime,
    @Serializable(with= LocalDateTimeKserialize::class)
    val cancelledAt:LocalDateTime?,
    val cancellationReason:String?,
    @Serializable(with= LocalDateTimeKserialize::class)
   val actualCheckOut:LocalDateTime?,
    val room:RoomDto,
    val user:UserDto
)
