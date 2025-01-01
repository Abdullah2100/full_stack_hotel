import { Dispatch, SetStateAction } from 'react'
import { UserModule } from '../../module/userModule';
import { Switch } from '@mui/material';
import NotFoundComponent from '../notFoundContent';
import { ArrowUturnLeftIcon, PencilIcon, TrashIcon } from '@heroicons/react/16/solid';
import { userAuthModule } from '../../module/userAuthModule';
import { Guid } from 'guid-typescript';
import { generalMessage } from '../../util/generalPrint';

interface UserTableProps {
  data?: UserModule[] | undefined,
  setUser: Dispatch<SetStateAction<userAuthModule>>
  seUpdate: Dispatch<SetStateAction<boolean>>
  seUserID: Dispatch<SetStateAction<Guid|undefined>>
  deleteFunc:(userId: Guid,isDeletion:boolean|undefined) => Promise<void>
  isShwoingDeleted: boolean
}

const UersTable = ({ data, setUser,seUpdate,seUserID ,deleteFunc,isShwoingDeleted=false}: UserTableProps) => {
  data?.forEach((x)=>{
    generalMessage("this the user data "+x.isVip)
  })

  const setUserData = (user: UserModule) => {
    
    setUser({
      address: user.personData?.address,
      brithDay:user.brithDay.split('T')[0], //dateHolder.toISOString().split('T')[0],
      email: user.personData?.email,
      name: user.personData?.name,
      password: '',
      phone: user.personData?.phone,
      username: user.userName,
    })
    seUpdate(true)
    seUserID(user.userId)
  }

  return (
    <div className={`overflow-x-auto justify-center ${data === undefined && 'h-48'} `}>
      <table className="min-w-full table-auto border-collapse">
        <thead className="bg-gray-200 text-gray-600">
          <tr>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">User ID</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Person ID</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Name</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Email</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Phone</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Address</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Created At</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Brithday</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">VIP Status</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Operation</th>
          </tr>
        </thead>
        <tbody>
          {data !== undefined && data.length > 0 && data.map(( user, index) => (
           user.isDeleted&&isShwoingDeleted===false?undefined: <tr
              key={index}
              className={ ` ${user.isDeleted?'bg-red-500':'bg-white'}`}
            >
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.userId || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personID || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.name || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.email || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.phone || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.address || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {user?.personData?.createdAt === undefined ? "" : new Date(user.personData.createdAt).toISOString().split('T')[0]}
              </td>
               <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {user?.personData?.createdAt === undefined ? "" : user.brithDay.split('T')[0]}
              </td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                <Switch
                  defaultChecked={user.isVip}
                  // disabled={true}
                  onChange={()=>deleteFunc(user.userId,undefined)}
                />
              </td>

              <td className="px-4 py-1 pt-4  text-left ">
                {user.isDeleted===false?
                <div className='flex flex-row justify-between'>

                <button 
                   onClick={()=>deleteFunc(user.userId,true)}
                   className='border-[2px] rounded-[3px] border-red-600 h-7 w-7 flex justify-center items-center'
                   ><TrashIcon className='h-4 w-4 text-red-600 ' /></button>
                <button
                onClick={() => setUserData(user)}
                className='border-[2px] rounded-[3px] border-green-800 h-7 w-7 flex justify-center items-center bg-gray-200'
                ><PencilIcon className='h-6 w-6 text-green-800' /></button>
                </div>:
                <button onClick={()=>deleteFunc(user.userId,false)}>

                <ArrowUturnLeftIcon 
                 className='h-6 w-6 text-white'/>
                 </button>
                }
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {data === undefined &&
        <div className='h-20 w-full flex flex-col justify-center items-center  mt-10' >

          <NotFoundComponent />
        </div>
      }

    </div>
  )
}

export default UersTable
