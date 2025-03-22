package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ImageDto
    (
    var id: String? = null,
    var path: String? = null,
    var belongTo: String? = null,
    var isDeleted: Boolean? = null,
    var isThumnail: Boolean? = null

)