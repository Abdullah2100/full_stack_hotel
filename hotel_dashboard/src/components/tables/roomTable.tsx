import { Dispatch, SetStateAction } from 'react'
import { RoomModule } from '../../module/RoomModule';
import { Switch } from '@mui/material';
import NotFoundComponent from '../notFoundContent';
import { ArrowUturnLeftIcon, PencilIcon, TrashIcon } from '@heroicons/react/16/solid';
import { IAuthModule } from '../../module/iAuthModule';
import { Guid } from 'guid-typescript';
import ImageHolder from '../imageHolder';
import { IRoomModule } from '../../module/iRoomModule';
import { generalMessage } from '../../util/generalPrint';
import DateFormat from '../../util/dateFormat';

interface RoomTableProps {
  data?: IRoomModule[] | undefined,
  setRoom: Dispatch<SetStateAction<IRoomModule>>
  setRoomHover: Dispatch<SetStateAction<IRoomModule | undefined>>
  seUpdate: Dispatch<SetStateAction<boolean>>
  deleteFunc: (roomId: Guid) => Promise<void>
  makeRoomVip: (roomId: Guid) => Promise<void>
  isShwoingDeleted: boolean

}

const RoomTable = ({
  data,
  setRoom,
  setRoomHover,
  seUpdate,
  deleteFunc,
  makeRoomVip,
  isShwoingDeleted = false
}: RoomTableProps) => {

  const setRoomData = (room: RoomModule) => {
    // setRoom({
    //   roomId: Room.RoomId,
    //   address: Room.personData?.address,
    //   brithDay: Room.brithDay.split('T')[0], //dateHolder.toISOString().split('T')[0],
    //   email: Room.personData?.email,
    //   name: Room.personData?.name,
    //   password: '',
    //   phone: Room.personData?.phone,
    //   Roomname: Room.RoomName,
    //   imagePath: Room.imagePath?.toString()
    // })
    seUpdate(true)
  }

  return (
    <div className={`overflow-x-auto justify-center ${data === undefined && 'h-48'} pb-7 `}>
      <table className="min-w-full table-auto border-collapse">
        <thead className="bg-gray-200 text-gray-600">
          <tr>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap"></th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Owner by</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">RoomType</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Created At</th>
            {/* <th className="px-4 py-2 border-b text-left whitespace-nowrap">Name</th> */}
            {/* <th className="px-4 py-2 border-b text-left whitespace-nowrap">Email</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Phone</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Address</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Created At</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Brithday</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">VIP Status</th>
            <th className="px-4 py-2 border-b text-left whitespace-nowrap">Operation</th> */}
          </tr>
        </thead>
        <tbody>
          {data !== undefined && data.length > 0 && data.map((Room, index) => (
            Room.isDeleted && isShwoingDeleted === false ? undefined : <tr
              key={index}
              className={` ${Room.isDeleted ? 'bg-red-500' : 'bg-white'}`}
            >
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{index + 1}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                <button
                  className='text-blue-600'
                  onClick={() => { setRoomHover(Room) }}
                  style={{ cursor: 'pointer' }}
                >
                  {
                    Room?.user?.personData.name || ""
                  }
                </button>


              </td>

              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{Room.roomData?.roomTypeName}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{DateFormat.toStringDate(Room.createdAt)}</td>

              {

                /*
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{

                <ImageHolder src={`http://172.19.0.1:9000/Room/${Room.imagePath}`}
                  style='flex flex-row h-20 w-20'
                />
              }</td>

              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{Room?.personData?.email || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{Room?.personData?.phone || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">{Room?.personData?.address || ""}</td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {Room?.personData?.createdAt === undefined ? "" : new Date(Room.personData.createdAt).toISOString().split('T')[0]}
              </td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                {Room?.personData?.createdAt === undefined ? "" : Room.brithDay.split('T')[0]}
              </td>
              <td className="px-4 py-2 border-b text-left whitespace-nowrap">
                <Switch
                  defaultChecked={Room.isVip}
                  disabled={Room.isDeleted}
                  onChange={() => makeRoomVip(Room.RoomId)}
                />
              </td>

              <td className="px-4 py-1   text-left ">
                {Room.isDeleted === false ?
                  <div className='flex flex-row justify-between'>

                    <button
                      onClick={() => deleteFunc(Room.RoomId)}
                      className='border-[2px] rounded-[3px] border-red-600 h-7 w-7 flex justify-center items-center'
                    ><TrashIcon className='h-4 w-4 text-red-600 ' /></button>
                    <button
                      onClick={() => setRoomData(Room)}
                      className='border-[2px] rounded-[3px] border-green-800 h-7 w-7 flex justify-center items-center bg-gray-200'
                    ><PencilIcon className='h-6 w-6 text-green-800' /></button>
                  </div> :
                  <button onClick={() => deleteFunc(Room.RoomId)}>

                    <ArrowUturnLeftIcon
                      className='h-6 w-6 text-white' />
                  </button>
                }
              </td> */}
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

export default RoomTable
