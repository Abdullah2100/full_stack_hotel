import React from 'react'
import { UserModule } from '../../module/userModule';
import { Switch } from '@mui/material';
import NotFoundComponent from '../notFoundContent';
import { PencilIcon, TrashIcon } from '@heroicons/react/16/solid';

interface UserTableProps {
  data?: UserModule[] | undefined
}
const UersTable = ({ data }: UserTableProps) => {


  return (
    <div className={`overflow-x-auto justify-center ${data===undefined&&'h-48'} `}>
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
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">VIP Status</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Operation</th>
          </tr>
        </thead>
        <tbody>
          {data !== undefined && data.length > 0 && data.map((user, index) => (
            <tr
              key={index}
              className={index % 2 === 0 ? "bg-white" : "bg-gray-50"}
            >
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.userId || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personID || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.name || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.email || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.phone || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.address || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {user?.personData?.createdAt === undefined ? "" : new Date(user.personData.createdAt).toLocaleString()}
              </td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                <Switch
                  value={user.isVip}
                  disabled={true}
                />
              </td>

              <td className="px-4 py-2 border-b text-left flex flex-row justify-between">
                <button 
                className='border-[2px] rounded-[3px] border-red-600 h-7 w-7 flex justify-center items-center'
                ><TrashIcon className='h-4 w-4 text-red-600 '/></button>
                  <button 
                className='border-[2px] rounded-[3px] border-green-800 h-7 w-7 flex justify-center items-center bg-gray-200'
              ><PencilIcon className='h-6 w-6 text-green-800'/></button>
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
