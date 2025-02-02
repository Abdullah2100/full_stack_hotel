import { Guid } from "guid-typescript";
import { iImageHolder } from "./IImageHolder";
import { enStatsu } from "./enStatsu";

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