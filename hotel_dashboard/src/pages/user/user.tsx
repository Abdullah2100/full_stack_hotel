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
import { Guid } from 'guid-typescript';
import { Switch } from '@mui/material';

const User = () => {
  const { showToastiFy } = useContext(useToastifiContext)

  const refreshToken = useSelector((state: RootState) => state.auth.refreshToken)

  const [status, setState] = useState<enStatus>(enStatus.none)
  const [page, setPage] = useState<number>(1)
  const [isNoData, setNoData] = useState<boolean>(false)
  const [isShowingDeleted, setShowingDeleted] = useState<boolean>(false)
  const [userId, setUserId] = useState<Guid | undefined>(undefined)

  const [userAuth, setUser] = useState<userAuthModule>({
    name: '',
    email: '',
    phone: '',
    address: '',
    username: '',
    password: '',
    brithDay: (new Date()).toISOString().split('T')[0],
  });

  const [isUpdate, setUpdate] = useState<boolean>(false)

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

  const userChangeStatus = useMutation({
    mutationFn: ({userId,isDeleation =false,endpoint}:{userId: Guid,isDeleation:boolean|undefined,endpoint:string}) =>
      apiClient({
        enType:isDeleation? enApiType.DELETE :enApiType.POST,
        endPoint:endpoint + '/' + userId
        ,
        isRquireAuth: true,
        jwtValue: refreshToken || ""
      }),
    onSuccess: (data) => {
      setState(enStatus.complate)
      showToastiFy(`user    Sueccessfuly`, enMessage.SECCESSFUL);

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


  const deleteUserFun = async (userId: Guid,isDeletion?:boolean|undefined) => {
    let endpoint = '';
    if(isDeletion!==undefined){
      endpoint = (isDeletion? import.meta.env.VITE_DELETEDTEUSERS:import.meta.env.VITE_UNDELETE_USER)
    }
    else{
      endpoint = import.meta.env.VITE_MAKEUSERVIP
    }

    await userChangeStatus.mutate({userId,isDeleation:isDeletion,endpoint})

  };


  const singup = useMutation({
    mutationFn: (userData: any) =>
      apiClient({
        enType: enApiType.POST,
        endPoint: isUpdate ?
          import.meta.env.VITE_UPDATEUSERS : import.meta.env.VITE_CreateUSERS
        , prameters: userData,
        isRquireAuth: true,
        jwtValue: refreshToken || ""
      }),
    onSuccess: (data) => {
      setState(enStatus.complate)
      showToastiFy(`user ${isUpdate ? "updated" : "created"} Sueccessfuly`, enMessage.SECCESSFUL);
      setUser({
        address: '',
        password: '',
        email: '',
        name: '',
        phone: '',
        username: '',
        brithDay: (new Date()).toISOString().split('T')[0]
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
    if (!isUpdate)
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
      "brithDay": new Date(userAuth.brithDay) || null,
      "isVip": false
    }
    if (isUpdate)
      data.Id = userId;

    if (isUpdate) data
    await singup.mutate(data)

  };




  useEffect(() => {

    if (error) {
      setNoData(true)
      showToastiFy(error.message, enMessage.ERROR);
    }
    if (data) {
      generalMessage("this the data from user " +JSON.stringify(data.data[0]))
      setNoData(false)
    }
  }, [error, data])


  useEffect(() => {
  }, [userAuth])


  useEffect(()=>{
  },[isShowingDeleted])



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
            style={`mb-1 me-2 w-32 w-full md:w-32 `}
            maxLength={50}
            isRequire={true}
          />
          <TextInput
            isDisabled={isUpdate}
            keyType='email'
            value={userAuth.email}
            onInput={updateInput}
            placeHolder="email"
            style={`mb-1  w-[139px] me-2 ${isUpdate && 'text-gray-400'}`}
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
            placeHolder={isUpdate ? 'update' : 'create'}
            style="text-[10px] bg-mainBg w-[120px] text-white rounded-[2px] mt-2 h-6"
          />

        </div>
        <h3 className='text-2xl font-bold mt-2'>users Data : </h3>

        <div className="overflow-x-auto   w-full mt-4 mb-5">
          <UersTable
            data={data !== undefined ? (data.data as UserModule[]) : undefined}
            setUser={setUser}
            seUpdate={setUpdate}
            seUserID={setUserId}
            deleteFunc={deleteUserFun}
            isShwoingDeleted={isShowingDeleted}
          />
        </div>
        <div>
          <h3>showing the deleted user</h3>
          <Switch onChange={()=>setShowingDeleted(prev=>!prev)} />
        </div>
      </div>


    </div>
  )
}

export default User