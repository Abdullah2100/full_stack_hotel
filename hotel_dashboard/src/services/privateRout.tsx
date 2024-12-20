import { useContext } from "react"
import { userAuthContext } from "../context/validLogin"
import { generalMessage } from "../util/generalPrint";
import Login from "../pages/login";



const PrivateRout = ({ Page }) => {
    const { hasValidToken } = useContext(userAuthContext)


    if (!hasValidToken) {
        generalMessage("this from  not have validation ")
        return <Login />
    }

    return <Page />

};

export default PrivateRout