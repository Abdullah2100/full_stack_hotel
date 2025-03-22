package com.example.hotel_mobile.Modle

import java.util.UUID

data class RoomModel(
    var roomId: String? = null,
    var status: Int? = null,
    var pricePerNight: Int? = null,
    var capacity: Int? = null,
    var roomtypeid: String? = null,
    var createdAt: String? = null,
    var bedNumber: Int? = null,
    var beglongTo: String? = null,
    var isBlock: Boolean? = null,
    var isDeleted: Boolean? = null,
    var user: UserModel? = null,
    var roomData: String? = null,
    var images: List<ImageModel>? = null,
    var roomTypeModel: RoomTypeModel? = null
)
