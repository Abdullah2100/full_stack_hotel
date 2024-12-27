import React, { useContext, useEffect, useState } from 'react'
import Header from '../../components/header/header'
import { UsersIcon } from '@heroicons/react/16/solid'
import { userAuthModule } from '../../module/userAuthModule';
import { TextInput } from '../../components/input/textInput';
import { PasswordInput } from '../../components/input/passwordInput';
import SubmitButton from '../../components/button/submitButton';
import { enStatus } from '../../module/enState';
import UersTable from '../../components/tables/usersTable';
import { notifyManager, useMutation, useQuery } from '@tanstack/react-query';
import apiClient from '../../services/apiClient';
import { enApiType } from '../../module/enApiType';
import NotFoundPage from '../NotFound/notfound';
import { useSelector } from 'react-redux';
import { RootState } from '../../controller/rootReducer';
import { generalMessage } from '../../util/generalPrint';
import { useToastifiContext } from '../../context/toastifyCustom';
import { enMessage } from '../../module/enMessageType';
import NotFoundComponent from '../../components/notFoundContent';
import { UserModule } from '../../module/userModule';
import { isHasCapitalLetter, isHasNumber, isHasSmallLetter, isHasSpicalCharacter, isValidEmail } from '../../util/regexValidation';

const User = () => {
  const { showToastiFy } = useContext(useToastifiContext)

  const refreshToken = useSelector((state: RootState) => state.auth.refreshToken)

  const [status, setState] = useState<enStatus>(enStatus.none)
  const [page, setPage] = useState<number>(1)
  const [isNoData, setNoData] = useState<boolean>(false)

  const [userAuth, setUser] = useState<userAuthModule>({
    name: 'asdf',
    email: 'asda@gmail.com',
    phone: '7755012257',
    address: 'sadf',
    username: 'asdf',
    password: 'asAS12#$',
    brithDay: undefined,
  });

  const updateInput = (value: any, key: string) => {
    setUser((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const { data, error, refetch } = useQuery({

    queryKey: ['users'],
    queryFn: async () => apiClient({
      enType: enApiType.GET,
      endPoint: import.meta.env.VITE_USERS + `${page}`,
      prameters: undefined,
      isRquireAuth: true,
      jwtValue: refreshToken || ""
    }),
  }
  );

  const singup = useMutation({
    mutationFn: (userData: any) =>
      apiClient({
        enType: enApiType.POST,
        endPoint: import.meta.env.VITE_CreateUSERS,
        prameters: userData,
        isRquireAuth: true,
        jwtValue: refreshToken || ""
      }),
    onSuccess: (data) => {
      setState(enStatus.complate)
      showToastiFy("user created Sueccessfuly", enMessage.SECCESSFUL);
      setUser({
        address: '',
        password: '',
        email: '',
        name: '',
        phone: '',
        username: '',
        brithDay: undefined
      })
      refetch();
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
    if (userAuth.name.trim().length < 1) {
      validationMessage = "name mustn't be empty"
    }
    else if (userAuth.email.trim().length < 1) {
      validationMessage = "email must not be empty"
    }
    else if (userAuth.brithDay === undefined) {
      validationMessage = "brithday must not be empty"
    }
    else if (userAuth.phone.length < 1) {
      validationMessage = "phone must not be empty"
    }
    else if (userAuth.username.length < 1) {
      validationMessage = "username must not be empty"
    }
    else if (userAuth.password.length < 1) {
      validationMessage = "password must not be empty"
    }
    else if (!isValidEmail(userAuth.email)) {
      validationMessage = "write valide email"
    }
    else if (userAuth.phone.length < 10) {
      validationMessage = "phone must atleast 10 numbers";
    }
    else if (!isHasCapitalLetter(userAuth.password)) {
      validationMessage = " password must contain 2 capital character"
    }
    else if (!isHasSmallLetter(userAuth.password)) {

      validationMessage = "password must contain 2 small character"
    }
    else if (!isHasSpicalCharacter(userAuth.password)) {
      validationMessage = "password must contain 2 special character";
    }
    else if (!isHasNumber(userAuth.password)) {
      validationMessage = " password must contain 2 number"
    }

    if (validationMessage.length > 0)
      showToastiFy(validationMessage, enMessage.ATTENSTION)

    return validationMessage.length > 0;
  }


  const createNewUser = async () => {
    if (validationInput()) {
      return;
    }
    setState(enStatus.loading)
    const data = {
      "name": userAuth.name,
      "email": userAuth.email,
      "phone": userAuth.phone,
      "address": userAuth.address,
      "userName": userAuth.username,
      "password": userAuth.password,
      "brithDay": userAuth.brithDay,
      "isVip": false
    }
    await singup.mutate(data)

  };




  useEffect(() => {

    if (error) {
      setNoData(true)
      showToastiFy(error.message, enMessage.ERROR);
    }
    if (data) {
      generalMessage(JSON.stringify(data.data))
      setNoData(false)
    }
  }, [error, data])

  return (
    <div className='flex flex-row'>
      {/* nav */}
      <Header index={1} />
      {/* main */}
      <div className='min-h-screen w-[calc(100%-192px)] ms-[192px] flex flex-col px-2 items-start  overflow-scroll '>
        <div className='flex flex-row items-center mt-2'>
          <UsersIcon className='h-8 fill-black group-hover:fill-gray-200 -ms-1' />
          <h3 className='text-2xl ms-1'>Users</h3>
        </div>
        <div className='mt-4 flex flex-row flex-wrap'>
          <TextInput

            keyType='name'
            value={userAuth.name}
            onInput={updateInput}
            placeHolder="name"
            style={"mb-1 me-2 w-32 w-full md:w-32"}
            maxLength={50}
            isRequire={true}
          />
          <TextInput
            keyType='email'
            value={userAuth.email}
            onInput={updateInput}
            placeHolder="email"
            style="mb-1 me-2 w-[139px]"
            maxLength={100}
            isRequire={true}

          />

          <TextInput
            keyType='brithDay'
            value={userAuth.brithDay}
            onInput={updateInput}
            placeHolder="2020/01/20"
            type="date"
            style="mb-1 me-2 w-[139px]"
            isRequire={true}

          />

          <TextInput
            keyType='phone'
            value={userAuth.phone}
            onInput={updateInput}
            placeHolder="735501225"
            style="mb-1 w-[119px] me-2"
            isRequire={true}
            maxLength={10}
            type='number'
          />
          <TextInput
            keyType='username'
            value={userAuth.username}
            onInput={updateInput}
            placeHolder="username"
            style="mb-1 w-[119px] me-2"
            isRequire={true}
            maxLength={50}

          />
          <PasswordInput
            keyType='password'
            value={userAuth.password}
            onInput={updateInput}
            placeHolder="*****"

            isRequire={true}
            canShowOrHidePassowrd={true}
            maxLength={8}
          />
          <div className='w-full '>

            <TextInput
              keyType='address'
              value={userAuth.address}
              onInput={updateInput}
              placeHolder="Yemen Sanaa"
              style="mb-1 w-full h-16"
              isRequire={true}
              isMultipleLine={true}
            />
          </div>

          <SubmitButton
            onSubmit={() => createNewUser()}
            buttonStatus={status}
            placeHolder={'create'}
            style="text-[10px] bg-mainBg w-[120px] text-white rounded-[2px] mt-2 h-6"
          />

        </div>
        <h3 className='text-2xl font-bold mt-2'>users Data : </h3>

        <div className="overflow-x-auto   w-full mt-4">
          <UersTable data={data !== undefined ? (data.data as UserModule[]) : undefined} />
        </div>
      </div>


    </div>
  )
}

export default User