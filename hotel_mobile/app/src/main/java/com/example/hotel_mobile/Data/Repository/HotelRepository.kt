package com.example.hotel_mobile.Data.Repository

import android.util.Log
import com.example.hotel_mobile.Dto.BookingDto
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.BookingModel
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.Util.General.toCustomString
import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.parameters
import kotlinx.serialization.Serializable
import java.io.IOException
import java.lang.reflect.Parameter
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton



class HotelRepository  @Inject constructor(private val httpClient: HttpClient) {


    suspend fun getRooms(pageNumber:Int): NetworkCallHandler {
        return try {
            val result = httpClient.get("${General.BASED_URL}/user/room/${pageNumber}")
            {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${General.authData.value?.refreshToken}")
                }
            }

            if (result.status == HttpStatusCode.OK) {
                val resultData = result.body<List<RoomDto>>();
                NetworkCallHandler.Successful(resultData)
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

    suspend fun createBooking (bookingModle:BookingDto): NetworkCallHandler {


        return try {

            val result = httpClient
                .post("${General.BASED_URL}/user/booking")
            {
                contentType(ContentType.Application.Json)
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer ${General.authData.value?.refreshToken}"
                    )
                }

                setBody(bookingModle)
            }


            if (result.status == HttpStatusCode.Created) {
                val resultData = result.body<String>();
                NetworkCallHandler.Successful(resultData)
            } else {
                NetworkCallHandler.Error(result.body())
            }

        } catch (e: UnknownHostException) {
            Log.d("bookingErrorIs",e.message.toString())

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {
            Log.d("bookingErrorIs",e.message.toString())

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {
            Log.d("bookingErrorIs",e.message.toString())

            return NetworkCallHandler.Error(e.message)
        }


    }


}

