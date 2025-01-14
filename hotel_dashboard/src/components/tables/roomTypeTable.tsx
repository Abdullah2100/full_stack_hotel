import { Dispatch, SetStateAction } from 'react'
import { Switch } from '@mui/material';
import NotFoundComponent from '../notFoundContent';
import { ArrowUturnLeftIcon, PencilIcon, TrashIcon } from '@heroicons/react/16/solid';
import { userAuthModule } from '../../module/userAuthModule';
import { Guid } from 'guid-typescript';
import ImageHolder from '../imageHolder';
import { IRoomType } from '../../module/roomModule';

interface RoomTypeTableProps {
  data?: IRoomType[],
  setRoomType: Dispatch<SetStateAction<IRoomType>>
  setUpdate: Dispatch<SetStateAction<boolean>>
  //deleteFunc: (userId: Guid, isDeletion: boolean | undefined) => Promise<void>
  isShwoingDeleted: boolean

}

const RoomTypeTable = ({
  data,
  setRoomType,
  setUpdate,
  isShwoingDeleted = false
}: RoomTypeTableProps) => {

  if (data !== undefined)
    console.log(`\n\nthis the roomtypes ${JSON.stringify(data)}\n\n`)

  const setUserData = (roomtype: IRoomType) => {
    setUpdate(true)
    setRoomType({
      roomTypeName: roomtype.roomTypeName,
      createdAt: null,
      createdBy: null,
      roomTypeID: roomtype.roomTypeID,
      imagePath: roomtype.imagePath
    })
  }

  return (
    <div className={`overflow-x-auto justify-center ${data === undefined && 'h-48'} `}>
      <table className="min-w-full table-auto border-collapse">
        <thead className="bg-gray-200 text-gray-600">
          <tr>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap"></th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Image</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Name</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Created At</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Operation</th>
          </tr>
        </thead>
        <tbody>
          {data !== undefined && data.length > 0 && data.map((roomtype, index) => (

            <tr
              key={index}
              className={` bg-white`}
            >
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{index + 1}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{

                <ImageHolder src={`http://172.19.0.1:9000/roomtype/${roomtype.imagePath}`}
                  style='flex flex-row h-20 w-20'
                />
              }</td>

              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{roomtype.roomTypeName}</td>

              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {roomtype?.createdAt ? new Date(roomtype.createdAt).toISOString().split('T')[0] : ""}
              </td>

              <td className="px-4 py-1   text-left ">
                {//user.isDeleted === false ?
                  <div className='flex flex-row justify-between'>

                    <button
                      // onClick={() => deleteFunc(user.userId, true)}
                      className='border-[2px] rounded-[3px] border-red-600 h-7 w-7 flex justify-center items-center'
                    ><TrashIcon className='h-4 w-4 text-red-600 ' /></button>
                    <button
                      onClick={() => setUserData(roomtype)}
                      className='border-[2px] rounded-[3px] border-green-800 h-7 w-7 flex justify-center items-center bg-gray-200'
                    ><PencilIcon className='h-6 w-6 text-green-800' /></button>
                  </div>
                  // :
                  // <button onClick={() => deleteFunc(user.userId, false)}>

                  //   <ArrowUturnLeftIcon
                  //     className='h-6 w-6 text-white' />
                  // </button>
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

export default RoomTypeTable
