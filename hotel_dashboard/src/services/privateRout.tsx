import { generalMessage } from "../util/generalPrint";
import Login from "../pages/login";
import { useSelector } from "react-redux";
import { RootState } from "../controller/rootReducer";



const PrivateRout = ({ Page }) => {
   const userAuth =   useSelector((state:RootState) => state.auth.refreshToken)

   generalMessage(`this from  not have validation ${userAuth}`)
    if (userAuth===null) {
        return <Login />
    }

    return <Page />
};

export default PrivateRout