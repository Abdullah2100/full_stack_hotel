import { Guid } from "guid-typescript";
import { PersonModule } from "./personModule";

export interface UserModule {
    userId: Guid,
    personID: Guid,
    brithDay: Date,
    isVip: boolean,
    userName: string,
    personData: PersonModule
}