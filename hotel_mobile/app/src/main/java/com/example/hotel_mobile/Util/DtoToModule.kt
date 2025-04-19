package com.example.hotel_mobile.Util

import com.example.hotel_mobile.Dto.ImageDto
import com.example.hotel_mobile.Dto.PersonDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Dto.RoomTypeDto
import com.example.hotel_mobile.Dto.UserDto
import com.example.hotel_mobile.Dto.response.BookingResponseDto
import com.example.hotel_mobile.Modle.response.BookingModel
import com.example.hotel_mobile.Modle.response.ImageModel
import com.example.hotel_mobile.Modle.response.PersonModel
import com.example.hotel_mobile.Modle.response.RoomModel
import com.example.hotel_mobile.Modle.response.RoomTypeModel
import com.example.hotel_mobile.Modle.response.UserModel

object DtoToModule {

    fun PersonDto.toPersonModle(): PersonModel {
        return PersonModel(
            name = this.name,
            personID = this.personID,
            createdAt = this.createdAt,
            email = this.email,
            phone = this.phone,
            address = this.address
        )
    }

    fun UserDto.toUserModel(): UserModel {
        return UserModel(
            isUser = this.isUser,
            userId = this.userId,
            userName = this.userName,
            personData = this.personData.toPersonModle(),
            addBy = this.addBy,
            isVip = this.isVip,
            brithDay = this.brithDay,
            imagePath = this.imagePath,
            password = this.imagePath ?: "",
            isDeleted = this.isDeleted,
        )
    }

    fun ImageDto.toImageModel(): ImageModel {
        return ImageModel(
            isDeleted = this.isDeleted,
            isThumnail = this.isThumnail,
            id = this.id,
            path = this.path,
            belongTo = this.belongTo
        )
    }

    fun RoomTypeDto.toRoomTypeModel(): RoomTypeModel {
        return RoomTypeModel(
            imagePath = this.imagePath,
            createdAt = this.createdAt,
            roomTypeName = this.roomTypeName,
            roomTypeID = this.roomTypeID,
            isDeleted = this.isDeleted,
            createdBy = this.createdBy
        )
    }

    fun RoomDto.toRoomModel(): RoomModel {
        return RoomModel(
            isDeleted = this.isDeleted,
            createdAt = this.createdAt,
            roomId = this.roomId,
            roomData = this.roomData,
            user = this.user?.toUserModel(),
            images = this.images?.map { it -> it.toImageModel() } ?: emptyList(),
            status = this.status,
            capacity = this.capacity,
            isBlock = this.isBlock,
            bedNumber = this.bedNumber,
            beglongTo = this.beglongTo,
            pricePerNight = this.pricePerNight,
            roomtypeid = this.roomtypeid,
            roomTypeModel = this.roomTypeData?.toRoomTypeModel(),
            location = this.location,
            longitude = this.longitude,
            latitude = this.latitude
        )
    }

    fun BookingResponseDto.toBookingModel(): BookingModel {
        return BookingModel(
            bookingId = this.bookingId,
            roomId = this.roomId,
            userId = this.userId,
            bookingStart = this.bookingStart,
            bookingEnd = this.bookingEnd,
            bookingStatus = this.bookingStatus,
            totalPrice = this.totalPrice,
            servicePayment = this.servicePayment,
            maintenancePayment = this.maintenancePayment,
            paymentStatus = this.paymentStatus,
            createdAt = this.createdAt,
            cancelledAt = this.cancelledAt,
            cancellationReason = this.cancellationReason,
            actualCheckOut = this.actualCheckOut,
            room = this.room.toRoomModel(),
            user = this.user.toUserModel()
        )
    }


}