package com.example.hotel_mobile.Modle.response

import java.time.LocalDateTime
import java.util.UUID

data class RoomTypeModel(
    val roomTypeID: UUID?,
    val roomTypeName: String,
    val imagePath: String? = null,
    val createdBy: UUID?=null,
    val createdAt: LocalDateTime?=null,
    val isDeleted: Boolean?=null
)
