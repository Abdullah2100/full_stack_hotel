package com.example.hotel_mobile.Modle

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("authModle")
data class AuthModle(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var token: String,
    var refreshToken: String
)