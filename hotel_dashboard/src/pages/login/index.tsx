
import { Link } from 'react-router-dom';
import SubmitButton from '../../components/button/submitButton';
import { TextInput } from '../../components/input/textInput';
import '../../index.css'
import { enStatus } from '../../module/enState';
import { userAuthLoginModule } from '../../module/userAuthLoginModule';
import { useContext, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useToastifiContext } from '../../context/toastifyCustom';
import apiClient from '../../services/apiClient';
import { enApiType } from '../../module/enApiType';
import { useMutation } from '@tanstack/react-query';
import { setTokens } from '../../controller/redux/jwtSlice';
import { AuthResult } from '../../module/userAuthResult';
import { enMessage } from '../../module/enMessageType';
import { PasswordInput } from '../../components/input/passwordInput';
const Login = () => {
    const dispatcher = useDispatch()
    const { showToastiFy } = useContext(useToastifiContext)

    const [status, setState] = useState<enStatus>(enStatus.none)

    const [authLoginModule, setUser] = useState<userAuthLoginModule>({
        eamilOrUserName: 'asda@gmail.com',
        password: 'asAS12#$'
    });

    const [enState, changeStatus] = useState<enStatus>(enStatus.none)

    const updateInput = (value: any, key: string) => {
        setUser((prev) => ({
            ...prev,
            [key]: value,
        }));
    };



    const singup = useMutation({
        mutationFn: (userData: any) =>
            apiClient({
                enType: enApiType.POST,
                endPoint: import.meta.env.VITE_SINGIN,
                prameters: userData
            }),
        onSuccess: (data) => {
            setState(enStatus.complate)
            const result = data.data as unknown as AuthResult;
            dispatcher(setTokens({ accessToken: result.accessToken, refreshToken: result.refreshToken }))
        },
        onError: (error) => {
            setState(enStatus.complate);

            if (error.response) {
                // Extract error message from the server response
                const errorMessage = error?.response || "An error occurred";
                showToastiFy(errorMessage, enMessage.ERROR);
            } else if (error.request) {
                // Handle network errors or no response received
                const requestError = "No response received from server";
                showToastiFy(requestError, enMessage.ERROR);
            } else {
                // Handle other unknown errors
                const unknownError = error.message || "An unknown error occurred";
                showToastiFy(unknownError, enMessage.ERROR);
            }
        }

    })

    const validationInput = () => {

        let validationMessage = "";
        if (authLoginModule.eamilOrUserName.trim().length < 1) {
            validationMessage = "email must not be empty"
        }
        else if (authLoginModule.password.trim().length < 1) {
            validationMessage = "password must not be empty"
        }

        if (validationMessage.length > 0)
            showToastiFy(validationMessage, enMessage.ATTENSTION)

        return validationMessage.length > 0;
    }
    const onSubmit = async () => {
        if (validationInput()) {
            return;
        }
        setState(enStatus.loading)
        const data = {
            "userNameOrEmail": authLoginModule.eamilOrUserName,
            "password": authLoginModule.password,
        }
        await singup.mutate(data)

    };

    return (
        <div className='flex flex-row '>

            <div className='md:h-screen md:w-1/2 bg-red-400 md:block hidden' />

            {/* login form */}
            <div className='h-screen md:w-1/2  w-screen px-5 pt-5 flex flex-col items-center justify-center' >
                <h3></h3>
                <TextInput
                    keyType='eamilOrUserName'
                    value={authLoginModule.eamilOrUserName}
                    onInput={updateInput}
                    placeHolder="Email"
                    style=" mb-2 w-[200px]"
                />
                <PasswordInput
                    keyType='Password'
                    value={authLoginModule.password}
                    onInput={updateInput}
                    placeHolder="*****"

                    isRequire={true}
                    canShowOrHidePassowrd={true}
                    maxLength={8}
                />


                <SubmitButton
                    buttonStatus={status}
                    placeHolder={'تسجيل الدخول'}
                    onSubmit={async () => { onSubmit() }}
                    style={'text-[10px] bg-mainBg w-[200px] text-white rounded-[4px]   mt-2  h-6 '} />
                <div className='w-[200px] justify-end '>

                    <Link to={'/signup'} className='text-[8px] text-black hover:text-blue-600'>
                        create new account
                    </Link>
                </div>
            </div>

        </div>
    )
}
export default Login;