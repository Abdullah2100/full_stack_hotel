package com.example.hotel_mobile.Util

import com.example.hotel_mobile.Dto.BookingDto
import com.example.hotel_mobile.Dto.ImageDto
import com.example.hotel_mobile.Dto.PersonDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Dto.RoomTypeDto
import com.example.hotel_mobile.Dto.UserDto
import com.example.hotel_mobile.Modle.BookingModel
import com.example.hotel_mobile.Modle.ImageModel
import com.example.hotel_mobile.Modle.PersonModel
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.RoomTypeModel
import com.example.hotel_mobile.Modle.UserModel
import com.example.hotel_mobile.Util.MoudelToDto.toUserDto
import java.time.LocalDateTime

object MoudelToDto {
    fun PersonModel.toPersonDto(): PersonDto {
        return PersonDto(
            name = this.name,
            personID = this.personID,
            createdAt = this.createdAt,
            email = this.email,
            phone = this.phone,
            address = this.address
        )
    }

    fun UserModel.toUserDto(): UserDto {
        return UserDto(
            isUser = this.isUser,
            userId = this.userId,
            userName = this.userName,
            personData = this.personData.toPersonDto(),
            addBy = this.addBy,
            isVip = this.isVip,
            brithDay = this.brithDay,
            imagePath = this.imagePath,
            password = this.imagePath ?: "",
            isDeleted = this.isDeleted,
        )
    }

    fun ImageModel.toImageDto(): ImageDto {
        return ImageDto(
            isDeleted = this.isDeleted,
            isThumnail = this.isThumnail,
            id = this.id,
            path = this.path,
            belongTo = this.belongTo
        )
    }

    fun RoomTypeModel.toRoomTypeDto(): RoomTypeDto {
        return RoomTypeDto(
            imagePath = this.imagePath,
            createdAt = this.createdAt,
            roomTypeName = this.roomTypeName,
            roomTypeID = this.roomTypeID,
            isDeleted = this.isDeleted,
            createdBy = this.createdBy
        )
    }

    fun RoomModel.toRoomDto(): RoomDto {
        return RoomDto(
            isDeleted = this.isDeleted,
            createdAt = this.createdAt,
            roomId = this.roomId,
            roomData = this.roomData,
            user = this.user?.toUserDto(),
            images = this.images?.map { images -> images.toImageDto() } ?: emptyList(),
            status = this.status,
            capacity = this.capacity,
            isBlock = this.isBlock,
            bedNumber = this.bedNumber,
            beglongTo = this.beglongTo,
            pricePerNight = this.pricePerNight,
            roomtypeid = this.roomtypeid,
            roomTypeData = this.roomTypeModel?.toRoomTypeDto()
        )
    }

    fun BookingModel.toBookingDto(): BookingDto {
        val startTimeList = this.startTime.split(":");
        val endTimeList = this.endTime.split(":");
        return BookingDto(
            bookingStartDateTime = LocalDateTime.of(
                this.startYear,
                this.startMonth,
                this.startDay,
                startTimeList[1].toInt(),
                startTimeList[2].toInt()
            ),
            bookingEndDateTime = LocalDateTime.of(
                this.endYear,
                this.endMonth,
                this.endDay,
                endTimeList[1].toInt(),
                endTimeList[2].toInt()
            ),
            roomId = this.roomId
        )
    }
}