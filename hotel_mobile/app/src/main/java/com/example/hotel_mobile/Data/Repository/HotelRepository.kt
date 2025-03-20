package com.example.hotel_mobile.Data.Repository

import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class HotelRepository  @Inject constructor(val httpClient: HttpClient) {

    suspend fun getRooms(pageNumber:Int): NetworkCallHandler {
        return try {
            val result = httpClient.post("${General.BASED_URL}/user/room/${pageNumber}") {
                contentType(ContentType.Application.Json)
            }

            if (result.status == HttpStatusCode.OK) {
                NetworkCallHandler.Successful(result.body<RoomDto>())
            } else {

                NetworkCallHandler.Error(result.body())
            }

        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }

    }
}