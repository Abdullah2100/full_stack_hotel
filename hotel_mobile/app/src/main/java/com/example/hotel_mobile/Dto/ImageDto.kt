package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ImageDto
    (
    @Serializable(with = UUIDKserialize::class)
    var id: UUID? = null,
    var path: String? = null,
    @Serializable(with = UUIDKserialize::class)
    var belongTo: UUID? = null,
    var isDeleted: Boolean? = null,
    var isThumnail: Boolean? = null

)