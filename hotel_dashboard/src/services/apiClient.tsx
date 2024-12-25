import axios from "axios";
import { enApiType } from "../module/enApiType";
import { enStatus } from "../module/enState";

interface ApiClientProps {
    enType: enApiType;
    endPoint: string;
    prameters?: any | undefined;
    jwtValue?: string | undefined;
    isFormData?: boolean | undefined;
    isRquireAuth?: boolean | undefined;

}
export default async function apiClient(
    {
        enType,
        prameters,
        endPoint,
        jwtValue = '',
        isFormData = false,
        isRquireAuth = false,
    }: ApiClientProps
) {
    const full_url = import.meta.env.VITE_BASE_URL + endPoint
    const header = handleHeader( isRquireAuth,isFormData, jwtValue)
    try {

        const repsonse = await axios({
            url: full_url,
            method: enType.toString(),
            data: prameters,
            headers: header
        });
        return repsonse;
    } catch (error) {
        throw {
            message:error?.response?.statusText|| error?.message,
            response: error?.response?.data,
            status: error?.response?.status,
        };
    }
}

function handleHeader(isRquireAuth: boolean, isFormData: boolean, jwtValue: string) {
    const header = {
        'Authorization': !isRquireAuth ? undefined : `Bearer ${jwtValue}`,
        "Content-Type": (isFormData ? 'multipart/form-data' : 'application/json')
    }

    return header;
}

