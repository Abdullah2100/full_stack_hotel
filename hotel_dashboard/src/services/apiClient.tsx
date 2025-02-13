import axios from "axios";
import { enApiType } from "../module/enApiType";
import { enStatus } from "../module/enState";
import { generalMessage } from "../util/generalPrint";

interface ApiClientProps {
    enType: enApiType;
    endPoint: string;
    prameters?: any | undefined;
    jwtValue?: string | undefined;
    jwtRefresh?: string | undefined;
    isFormData?: boolean | undefined;
    isRquireAuth?: boolean | undefined;
    tryNumber?: number | undefined;
}

export default async function apiClient(
    {
        enType,
        prameters,
        endPoint,
        jwtValue = '',
        jwtRefresh = '',
        isFormData = false,
        isRquireAuth = false,
        tryNumber = 1
    }: ApiClientProps
) {
    const full_url = import.meta.env.VITE_BASE_URL + endPoint
    const header = handleHeader(isRquireAuth, isFormData, jwtValue)
    generalMessage(full_url)
    try {

        const repsonse = await axios({
            url: full_url,
            method: enType.toString(),
            data: prameters,
            headers: header
        });
        return repsonse;
    } catch (error) {
        if (error?.response?.status === 401 && tryNumber == 1) {
            apiClient({
                enType: enType,
                prameters: prameters,
                jwtValue: jwtValue,
                jwtRefresh: jwtRefresh,
                endPoint: endPoint,
                isFormData: isFormData,
                isRquireAuth: isRquireAuth,
                tryNumber: 2,
            })
        } else {

            throw {
                message: error?.response?.data || error?.response?.statusText || error?.message,
                response: error?.response?.data,
                status: error?.response?.status,
                
            };
        }
    }
}

function handleHeader(isRquireAuth: boolean, isFormData: boolean, jwtValue: string) {
    const header = {
        'Authorization': !isRquireAuth ? undefined : `Bearer ${jwtValue}`,
        "Content-Type": (isFormData ? 'multipart/form-data' : 'application/json')
    }

    return header;
}

