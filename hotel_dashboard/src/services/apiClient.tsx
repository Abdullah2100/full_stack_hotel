import axios from "axios";
import { enApiType } from "../module/enApiType";
import { enStatus } from "../module/enState";

interface ApiClientProps {
    enType: enApiType;
    prameters?: any | undefined;
    endPoint: string;
    jwtValue?: string | undefined;
    isFormData?: boolean | undefined;
    isRquireAuth?: boolean | undefined;

}
const ApiClient = async (
    {
        enType,
        prameters,
        endPoint,
        jwtValue = '',
        isFormData = false,
        isRquireAuth = false,
    }: ApiClientProps
) => {
    var header = handleHeader(isFormData, isRquireAuth, jwtValue)


    let result;
    switch (enType) {
        case enApiType.DELETE: {
            result = axios.delete(endPoint, {
                headers: header,
                params: prameters
            }
            )

        } break;
        case enApiType.GET: {
            result = axios.get(endPoint, { headers: header, params: prameters })
        } break;
        case enApiType.PUT: {
            result = axios.put(endPoint, { headers: header, params: prameters })
        } break;
        default: {
            result = axios.post(endPoint, {
                headers: header,
                params: prameters
            }
            )
        } break;
    }
    return (await result).data
}

function handleHeader(isRquireAuth: boolean, isFormData: boolean, jwtValue: string) {
    var header = {
        'Authorization': !isRquireAuth ? undefined : `Bearer ${jwtValue}`,
        "Content-Type": (isFormData ? 'multipart/form-data' : 'application/json')
    }

    return header;
}