package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RoomDto(
    @Serializable(with = UUIDKserialize::class)
    var roomId: UUID? = null,
    var status: Int? = null,
    var pricePerNight: Int? = null,
    var capacity: Int? = null,
    var roomtypeid: String? = null,
    var createdAt: String? = null,
    var bedNumber: Int? = null,
    @Serializable(with = UUIDKserialize::class)
    var beglongTo: UUID? = null,
    var isBlock: Boolean? = null,
    var isDeleted: Boolean? = null,
    var user: UserDto? = null,
    var roomData: String? = null,
    var images: ArrayList<ImageDto>? = null,
    var 
)
