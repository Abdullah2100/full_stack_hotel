import axios from "axios"
import { generalMessage } from "../util/generalPrint";
import { userAuthModule } from "../module/userAuthModule";
import { AuthResult } from "../module/userAuthResult";
import { enStatus } from "../module/enState";

export const isValidToken = async (token: string) => {
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

export const signUpNewUser = async (userData: userAuthModule, changeState: (status: enStatus) => void) => {
    let isSignup = false;
    changeState(enStatus.loading)
    await axios.post(
        `${import.meta.env.BASE_URL}${import.meta.env.LOGIN_USER}`,
        {
            name: userData.username,
            email: userData.email,
            phone: userData.phone,
            address: userData.address,
            userName: userData.username,
            password: userData.password,
            brithDay: userData.brithDay
        })
        .then((data) => {
            changeState(enStatus.complate)

            if (data !== undefined) {
                var result = data as unknown as AuthResult;
                generalMessage(result.accessToken)
                generalMessage(result.refreshToken)

            }
        })
        .catch((error) => {
            
            changeState(enStatus.complate)
            generalMessage(error, true)

        });


}