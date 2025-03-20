package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RoomTypeDto(
    @Serializable(with = UUIDKserialize::class)
    val roomTypeID: UUID?,
    val roomTypeName: String,
    val imagePath: String? = null,
    val createdBy: String,
    @Serializable(with = LocalDateTimeKserialize::class)
    val createdAt: LocalDateTime,
    val isDeleted: Boolean
)
