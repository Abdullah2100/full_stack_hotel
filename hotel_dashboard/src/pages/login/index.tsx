
import { Link } from 'react-router-dom';
import SubmitButton from '../../components/button/submitButton';
import { TextInput } from '../../components/input/textInput';
import '../../index.css'
import { enStatus } from '../../module/enState';
import { userAuthLoginModule } from '../../module/userAuthLoginModule';
import { useState } from 'react';
const Login = () => {
    const [authLoginModule, setUser] = useState<userAuthLoginModule>({
        eamilOrUserName: '',
        password: ''
    });

    const [enState,changeStatus] = useState<enStatus>(enStatus.none)

    const updateInput = (value: any, key: string) => {
        setUser((prev) => ({
            ...prev,
            [key]: value,
        }));
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
                    placeHolder="email"
                    style="w-[150px] mb-2"
                />       
                 <TextInput
                keyType='password'
                value={authLoginModule.password}
                onInput={updateInput}
                placeHolder="****"
                type='password'
                style="w-[150px] mb-2"
            />
                
                 <SubmitButton
                    //buttonStatus={enStatus.loading}
                    placeHolder={'تسجيل الدخول'}
                    onSubmit={() => { }}
                    style={'text-[10px] bg-mainBg w-[150px] text-white rounded-[4px]   mt-2  h-6 '} />
                <div className='w-[150px] justify-end '>

                    <Link to={'/signup'} className='text-[8px] text-black hover:text-blue-600'>
                        create new account
                    </Link>
                </div>
            </div>

        </div>
    )
}
export default Login;