package com.example.hotel_mobile.Data.Repository

import android.util.Log
import com.example.hotel_mobile.Dto.request.BookingRequestDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Dto.response.BookingResponseDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject


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

    suspend fun  getBookingDayAtSpecficMonthAndYear(year:Int,month:Int):
    NetworkCallHandler {

        return try {

            val result = httpClient
                .post("${General.BASED_URL}/user/booking/between$year&${month+1}")
                {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${General.authData.value?.refreshToken}"
                        )
                    }

                }


            if (result.status == HttpStatusCode.OK) {
                val resultData = result.body<List<String>>();
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

    suspend fun createBooking (bookingModle: BookingRequestDto): NetworkCallHandler {


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

    suspend fun getUserBookings (pageNumber:Int): NetworkCallHandler {
        return try {

            val result = httpClient
                .get("${General.BASED_URL}/user/booking/${pageNumber}")
                {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            "Bearer ${General.authData.value?.refreshToken}"
                        )
                    }
                }

            if (result.status == HttpStatusCode.Created) {
                val resultData = result.body<List<BookingResponseDto>>();
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

