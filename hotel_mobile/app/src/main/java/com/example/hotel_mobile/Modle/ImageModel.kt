package com.example.hotel_mobile.Modle

import java.util.UUID

data class ImageModel
    (
    var id: String? = null,
    var path: String? = null,
    var belongTo: String? = null,
    var isDeleted: Boolean? = null,
    var isThumnail: Boolean? = null

)