import axios from "axios"
import { generalMessage } from "../util/generalPrint";

export const isValidToken = async(token: string) => {
    let isValid = false;
   await axios.post('/auth-validation', {
        tokenPram: token
    }).then((result) => {
        isValid = true;
    })
        .catch((error) => {

            generalMessage(error, true)
        })
        return isValid;
}