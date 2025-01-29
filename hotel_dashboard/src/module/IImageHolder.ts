import { Guid } from "guid-typescript";

export interface iImageHolder {
    image: IImageData,
    data?: File | undefined
}


export interface IImageData {
    id?: Guid | undefined
    path?: string | undefined
    belongTo?: Guid | undefined
    isDeleted?: boolean | undefined
    isThumnail?: boolean | undefined
}

