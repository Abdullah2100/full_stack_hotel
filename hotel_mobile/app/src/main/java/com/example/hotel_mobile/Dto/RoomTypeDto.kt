package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class RoomTypeDto(
    @Serializable(with = UUIDKserialize::class)
    val roomTypeID: UUID?,
    val roomTypeName: String,
    val imagePath: String? = null,
    @Serializable(with = UUIDKserialize::class)
    val createdBy: UUID?=null,
    @Serializable(with = LocalDateTimeKserialize::class)
    val createdAt: LocalDateTime?=null,
    val isDeleted: Boolean?=null
)
