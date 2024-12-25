import React from 'react'
import { UserModule } from '../../module/userModule';
import { Switch } from '@mui/material';

interface UserTableProps{
  data:UserModule[]
}
const UersTable = ({data}:UserTableProps) => {
   

  return (
    <div className="overflow-x-auto ">
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
        </tr>
      </thead>
      <tbody>
        {data.length>0&&data.map((user, index) => (
          <tr
            key={index}
            className={index % 2 === 0 ? "bg-white" : "bg-gray-50"}
          >
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.userId||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personID||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.name||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.email||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.phone||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">{user?.personData?.address||""}</td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">
              {user?.personData?.createdAt===undefined?"":new Date(user.personData.createdAt).toLocaleString()}
            </td>
            <td className="px-4 py-2 border-b text-left whitespace-nowrap">
              <Switch
              value={user.isVip}
              
              />
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
  )
}

export default UersTable
