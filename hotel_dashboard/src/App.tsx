import { BrowserRouter, Route, Routes } from "react-router-dom";
import NotFoundPage from "./pages/NotFound/notfound";
import { useContext } from "react";
import userAuthContext from "./context/validLogin";
import PrivateRout from "./services/privateRout";
import Home from "./pages/home";
import Login from "./pages/login";
import SignUp from "./pages/signUp";

const App = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={
                    <PrivateRout Page={<Home />}
                        isRequriedAdmin={false} />
                } />

                <Route path='/login' element={
                    <PrivateRout Page={
                        <Login />
                    }
                        isNavigateToLogin={true}
                    />
                } />

                <Route path='/signup' element={
                        <SignUp />
                } />
                <Route path='*' element={<NotFoundPage />} />
            </Routes>
        </BrowserRouter>
    )
}
export default App;