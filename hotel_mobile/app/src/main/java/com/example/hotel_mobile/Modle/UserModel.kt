package com.example.hotel_mobile.Modle

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class UserModel(
    var userId: String? = null,
    var addBy: String? = null,  // Nullable UUID
    var brithDay: LocalDateTime? = null,
    var isVip: Boolean? = false,
    var personData: PersonModel,
    var userName: String,
    var password: String,
    var isDeleted: Boolean,
    var imagePath: String? = null,
    var isUser: Boolean? = true
)