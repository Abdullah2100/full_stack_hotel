import { Guid } from "guid-typescript";
import { PersonModule } from "./personModule";

export interface UserModule {
    userId: Guid,
    personID: Guid,
    brithDay: string
    isVip: boolean,
    userName: string,
    isDeleted:boolean,
    personData: PersonModule,
    imagePath:string|null
}