import { Guid } from "guid-typescript";
import { enStatsu } from "./enRoomStatus";
import { iImageHolder } from "./IImageHolder";

export interface IRoomModule {
    roomId?: Guid | undefined

    status: enStatsu,

    pricePerNight: number

    capacity: number

    roomtypeid?: Guid | undefined
    beglongTo?: Guid | undefined

    bedNumber: number
    
    createdAt?: Date | undefined


    images?: iImageHolder[] | undefined


}