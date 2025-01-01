import { Guid } from "guid-typescript";

export interface userAuthModule {
    name: string
    email: string;
    phone: string;
    address: string;
    username: string;
    password: string;
    brithDay: string ;
    imagePath?:File|undefined;
}