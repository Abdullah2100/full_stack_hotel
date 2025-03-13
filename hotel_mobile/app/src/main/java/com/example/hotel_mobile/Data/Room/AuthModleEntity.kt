package com.example.hotel_mobile.Data.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AuthModleEntity
    (
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val token: String = "",
    val refreshToken: String = ""
)