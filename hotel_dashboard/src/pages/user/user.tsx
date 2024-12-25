import React, { useContext, useEffect, useState } from 'react'
import Header from '../../components/header/header'
import { UsersIcon } from '@heroicons/react/16/solid'
import { userAuthModule } from '../../module/userAuthModule';
import { TextInput } from '../../components/input/textInput';
import { PasswordInput } from '../../components/input/passwordInput';
import SubmitButton from '../../components/button/submitButton';
import { enStatus } from '../../module/enState';
import UersTable from '../../components/tables/usersTable';
import { notifyManager, useQuery } from '@tanstack/react-query';
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

  const { data, error } = useQuery({

    queryKey: ['users'],
    queryFn: async () => apiClient({
      enType: enApiType.GET,
      endPoint: import.meta.env.VITE_USERS + `${page}`,
      prameters: undefined,
      isRquireAuth: true,
      jwtValue: refreshToken || ""
    }),
    // refetchInterval:10000

  }
  );

  useEffect(() => {
    generalMessage(error)

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
          {<SubmitButton
            //onSubmit={() => onSubmit()}
            onSubmit={async () => { }}
            buttonStatus={status}
            placeHolder={'create'}
            // onSubmit={() => { }}
            style="text-[10px] bg-mainBg w-[120px] text-white rounded-[2px] mt-2 h-6"
          />}
        </div>
        <h3 className='text-2xl font-bold mt-2'>users Data : </h3>
        {isNoData ? <div className='w-full mt-4 h-40 flex flex-col items-center justify-center'>
          <NotFoundComponent />
        </div> :
          <div className="overflow-x-auto   w-full mt-4">
            <UersTable data={data !== undefined ? (data.data as UserModule[]) : []} />
          </div>}
      </div>


    </div>
  )
}

export default User