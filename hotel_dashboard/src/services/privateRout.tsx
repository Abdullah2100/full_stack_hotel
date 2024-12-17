import { Component, useContext } from "react"
import { userAuthContext } from "../context/validLogin"
import { Navigate, Route } from 'react-router-dom'
import { generalMessage } from "../util/generalPrint";
import Login from "../pages/login";



const PrivateRout = ({
    Page,
    isRequriedAdmin = false,
    isNavigateToLogin = false,
    isSignup = false }) => {
    const { hasValidToken } = useContext(userAuthContext)

    if (isSignup) {
        generalMessage("this from  signUp page")

        return <Page />
    }

    if (!hasValidToken) {
        generalMessage("this from  not have validation ")
        return <Login />
    }

    if (isNavigateToLogin && hasValidToken) {
        generalMessage("this from  navigate to login while has valid token ")
        return <Navigate to={'/'} />
    }

    if (!isRequriedAdmin) {
        generalMessage("this from  router to admin ")

        return <Navigate to={'*'} />
    }
    generalMessage("this from navigate to component")


    return <Page />

};

export default PrivateRout