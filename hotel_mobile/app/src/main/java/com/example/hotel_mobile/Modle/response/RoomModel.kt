package com.example.hotel_mobile.Modle.response

import java.util.UUID

data class RoomModel(
    var roomId: UUID? = null,
    var status: Int? = null,
    var pricePerNight: Float? = null,
    var capacity: Int? = null,
    var roomtypeid: UUID? = null,
    var createdAt: String? = null,
    var bedNumber: Int? = null,
    var beglongTo: UUID? = null,
    var isBlock: Boolean? = null,
    var isDeleted: Boolean? = null,
    var user: UserModel? = null,
    var roomData: String? = null,
    var images: List<ImageModel>? = null,
    var roomTypeModel: RoomTypeModel? = null,
    var location:String?,
    var longitude:Double?,
    val latitude:Double?
)
