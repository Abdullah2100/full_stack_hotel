package com.example.hotel_mobile.Modle

import java.util.UUID

data class ImageModel
    (
    var id: UUID? = null,
    var path: String? = null,
    var belongTo: UUID? = null,
    var isDeleted: Boolean? = null,
    var isThumnail: Boolean? = null

)