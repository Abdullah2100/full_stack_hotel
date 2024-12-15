import { createContext, ReactNode, useEffect, useState } from "react";
import { isValidToken } from "../services/authServices";

interface iUserAuthReq{
    isLogin:boolean,
    hasValidToken:boolean,
 
}
let userAuth:iUserAuthReq={
hasValidToken:false,
isLogin:false
}

interface UseAuthUserProps {
  children: ReactNode; // Type for the children prop
}

export const userAuthContext = createContext<iUserAuthReq>(userAuth);

const UseAuthUser = ({children}:UseAuthUserProps)=>{
    let accessToken = localStorage.getItem('access_token')||'';
    let [isLogin,changeLoginState] = useState((accessToken!==undefined&&accessToken?.length>0)||false);
    let [hasValidToken,chageTokenValidation] = useState(false);
    
    //async function validateToken(token:string){

   // }
   useEffect(()=>{
    if(isLogin){
       isValidToken(accessToken).then(()=>{
        chageTokenValidation(true)
       })
       .catch(()=>{
        chageTokenValidation(false)
       })
    }

   },[])


    return (
        <userAuthContext.Provider value={{isLogin,hasValidToken}}>
            {children}
        </userAuthContext.Provider>
    )

}

export default UseAuthUser;
