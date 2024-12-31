import { useContext } from "react"
import { userAuthContext } from "../context/validLogin"
import { Navigate, Route } from 'react-router-dom'
import { generalMessage } from "../util/generalPrint";
import Login from "../pages/login";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../controller/rootReducer";
import { setTokens } from "../controller/redux/jwtSlice";

const AuthRoute = ({ Page }) => {
    const userAuth =   useSelector((state:RootState) => state.auth.refreshToken)
    

    if (userAuth!==null)
        return <Navigate to={'/'} />

    return (<Page />)
}

export default AuthRoute
        
    