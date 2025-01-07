import { useState } from 'react';
import Header from '../../components/header/header'
import { TextInput } from '../../components/input/textInput'
import { IRoomType } from '../../module/roomModule';
import { RectangleGroupIcon } from '@heroicons/react/16/solid';
import SubmitButton from '../../components/button/submitButton';

const RoomType = () => {
  const [isUpdate, setUpdate] = useState<boolean>(false)

  const [roomType, setRoomType] = useState<IRoomType>({
    roomTypeName: '',
    createdAt: null,
    createdBy: null,
    roomTypeID: null
  });

  const updateInput = (value: any, key: string) => {
    setRoomType((prev) => ({
      ...prev,
      [key]: value,
    }));
  };
  return (
    <div className='flex flex-row'>

      <Header index={2} />

      <div className='min-h-screen w-[calc(100%-192px)] ms-[192px] flex flex-col px-2 items-start  overflow-scroll '>
        <div className='flex flex-row items-center mt-2'>
          <RectangleGroupIcon className='h-8 fill-black group-hover:fill-gray-200 -ms-1' />
          <h3 className='text-2xl ms-1'>RoomType</h3>
        </div>

        <div className='mt-4 flex flex-row flex-wrap gap-1 w-full'>
          <TextInput
            keyType='roomTypeName'
            value={roomType.roomTypeName}
            onInput={updateInput}
            placeHolder="name"
            style={`mb-1 w-full`}
            handleWidth='w-full'

            maxLength={50}
            isRequire={true}
          />

        </div>
          <SubmitButton
            onSubmit={async () => { }}
            // buttonStatus={status}
            placeHolder={isUpdate ? 'update' : 'create'}
            style="text-[10px] bg-mainBg  w-full  text-white rounded-[2px] mt-2 h-6"
          />
      </div>
    </div>
  )
}

export default RoomType