package com.example.hotel_mobile.Util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.hotel_mobile.Dto.RoomDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object RoomNavType {

    val RoomType = object : NavType<RoomDto>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): RoomDto? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): RoomDto {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: RoomDto): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: RoomDto) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
