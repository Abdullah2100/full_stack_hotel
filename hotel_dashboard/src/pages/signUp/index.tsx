import React, { useState } from 'react'
import { TextInput } from '../../components/input/textInput'
import SubmitButton from '../../components/button/submitButton'
import { Link } from 'react-router-dom'
import { userAuthModule } from '../../module/userAuthModule';

const SignUp = () => {
    const [userAuth, setUser] = useState<userAuthModule>({
        name: '',
        email: '',
        phone: '',
        address: '',
        username: '',
        password: '',
        brithDay: new Date(),
    });

    // Update input field in the userAuth state by key
    const updateInput = (value: any, key: string) => {
        setUser((prev) => ({
            ...prev,
            [key]: value,
        }));
    };

    return (
        <div className="h-screen w-screen flex flex-col items-center justify-center">
            <TextInput
                keyType='email'
                value={userAuth.email}
                onInput={updateInput}
                placeHolder="email"
                style="w-[150px] mb-2"
            />

            <TextInput
                keyType='username'
                value={userAuth.username}
                onInput={updateInput}
                placeHolder="username"
                style="w-[150px] mb-2"
            />

            <TextInput
                keyType='brithDay'
                value={userAuth.brithDay}
                onInput={updateInput}
                placeHolder="2020/01/20"
                type="date"
                style="w-[150px] mb-2"
            />

            <TextInput
                keyType='phone'
                value={userAuth.phone}
                onInput={updateInput}
                placeHolder="735501225"
                style="w-[150px] mb-2"

            />

            <TextInput
                keyType='password'
                value={userAuth.password}
                onInput={updateInput}
                placeHolder="*****"
                style="w-[150px]"
                type="password"
            />

            <SubmitButton
                placeHolder={'Sign Up'}
                onSubmit={() => { }}
                style="text-[10px] bg-mainBg w-[150px] text-white rounded-[4px] mt-2 h-6"
            />
            <div className="w-[150px] justify-end">
                <Link to={'/login'} className="text-[8px] text-black hover:text-blue-600">
                    Go to login
                </Link>
            </div>
        </div>
    );
};

export default SignUp;
