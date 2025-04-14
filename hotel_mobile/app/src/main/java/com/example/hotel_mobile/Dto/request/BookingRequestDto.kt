package com.example.hotel_mobile.Dto.request


import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID


@Serializable
data class BookingRequestDto(
    @Serializable(with = UUIDKserialize::class)
    var roomId: UUID,
    @Serializable(with = LocalDateTimeKserialize::class)
    var bookingStartDateTime: LocalDateTime,
    @Serializable(with = LocalDateTimeKserialize::class)
    var bookingEndDateTime: LocalDateTime
)
