package com.example.hotel_mobile.Modle.Request

import java.io.File
import java.util.UUID

data class RoomImageCreation(
    val id: UUID?,
    val data: File,
    val belongTo: UUID?,
    val isDeleted: Boolean?,
    var isThumbnail: Boolean?
)
