package com.example.hotel_mobile.Modle.Request

import java.util.UUID

data class RoomCreationModel(
    var pricePerNight: Double?=null,
    var capacity: Int? = null,
    var roomtypeid: UUID? = null,
    var bedNumber: Int? = null ,
    var images: MutableList<RoomImageCreation>? = null,
    var location: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)
