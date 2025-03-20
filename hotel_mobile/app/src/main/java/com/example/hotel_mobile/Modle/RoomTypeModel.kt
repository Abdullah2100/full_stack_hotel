package com.example.hotel_mobile.Modle

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

data class RoomTypeModel(
    val roomTypeID: UUID?,
    val roomTypeName: String,
    val imagePath: String? = null,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val isDeleted: Boolean
)
