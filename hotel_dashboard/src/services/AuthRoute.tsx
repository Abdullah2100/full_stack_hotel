import { useContext } from "react"
import { userAuthContext } from "../context/validLogin"
import { Navigate, Route } from 'react-router-dom'
import { generalMessage } from "../util/generalPrint";
import Login from "../pages/login";

const AuthRoute = ({ Page }) => {
    const { hasValidToken } = useContext(userAuthContext)

    if (hasValidToken)
        return <Navigate to={'/'} />

    return (<Page />)
}

export default AuthRoute
        
    