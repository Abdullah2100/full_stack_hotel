package com.example.hotel_mobile.Data.Repository

import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters
import java.io.IOException
import java.lang.reflect.Parameter
import java.net.UnknownHostException
import javax.inject.Inject

class HotelRepository  @Inject constructor(private val httpClient: HttpClient) {






    suspend fun getRooms(pageNumber:Int): NetworkCallHandler {
        return try {
            val result = httpClient.get("${General.BASED_URL}/user/room/${pageNumber}") {
//                    contentType(ContentType.Application.Json)
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
        finally {
            httpClient.close()
        }

    }
}