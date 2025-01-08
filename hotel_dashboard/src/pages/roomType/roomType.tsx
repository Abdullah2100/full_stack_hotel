import { useContext, useRef, useState } from 'react';
import Header from '../../components/header/header'
import { TextInput } from '../../components/input/textInput'
import { IRoomType } from '../../module/roomModule';
import { PencilIcon, RectangleGroupIcon } from '@heroicons/react/16/solid';
import SubmitButton from '../../components/button/submitButton';
import { Button } from '@mui/material';
import { enMessage } from '../../module/enMessageType';
import { useToastifiContext } from '../../context/toastifyCustom';

const RoomType = () => {
  const { showToastiFy } = useContext(useToastifiContext)

  const [isUpdate, setUpdate] = useState<boolean>(false)
  const imageRef = useRef<HTMLInputElement>(null);
  const imageImageRef = useRef<HTMLImageElement>(null);
  const [image, setImage] = useState<string | undefined>(undefined)

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



  const selectImage = (e) => {
    e.preventDefault();
    imageRef.current?.click();
  }

  const uploadImageDisplay = async (e) => {
    if (imageRef.current && imageRef.current.files && imageRef.current.files[0]) {

      const uploadedFile = imageRef.current.files[0];
      const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();
      if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
        showToastiFy("you must select valide image ", enMessage.ERROR)
        return;
      }
      showToastiFy(`this the image height ${e.height} \n
  this the image width ${e.height}`, enMessage.ERROR)
      if (e.height > 40 && e.width > 40) {
        showToastiFy("you must select image with 40 height and width ", enMessage.ERROR)
        return;
      }
      const cachedURL = URL.createObjectURL(uploadedFile);
      ///generalMessage("this the image url " + cachedURL)
      setImage(cachedURL);

    }
  }

  return (
    <div className='flex flex-row'>

      <Header index={2} />

      <div className='min-h-screen w-[calc(100%-192px)] ms-[192px] flex flex-col px-2 items-start  overflow-scroll '>
        <div className='flex flex-row items-center mt-2'>
          <RectangleGroupIcon className='h-8 fill-black group-hover:fill-gray-200 -ms-1' />
          <h3 className='text-2xl ms-1'>RoomType</h3>
        </div>

        <div className='mt-4 flex flex-row flex-wrap gap-1 w-full'>
          <div className='w-full flex flex-col-reverse md:flex-row md:justify-between mb-4'>
            <input
              type="file"
              id="file"
              ref={imageRef}
              onChange={uploadImageDisplay}
              hidden />

            <TextInput
              keyType='roomTypeName'
              value={roomType.roomTypeName}
              onInput={updateInput}
              placeHolder="Name"
              style={`mb-1 w-full`}
              handleWidth='w-full md:w-[200px]'

              maxLength={50}
              isRequire={true}
            />

            <div
              className=' h-40 w-40 mb-2 md:mb-0 border-[2px] border-dashed border-gray-200 rounded-sm relative'>
              <button
                onClick={selectImage}
                className='group absolute end-1 top-2 hover:bg-gray-600 hover:rounded-sm '>
                <PencilIcon className='h-6 w-6 border-[1px] border-blue-900 rounded-sm group-hover:fill-gray-200  ' />
              </button>
              {image && <img ref={imageImageRef} src={image} />}

            </div>

          </div>


        </div>
        <div className={'flex flex-row gap-3'}>
          <SubmitButton
            onSubmit={async () => { }}
            // buttonStatus={status}
            placeHolder={isUpdate ? 'update' : 'create'}
            style="text-[10px] bg-mainBg   w-[90px]  text-white rounded-[2px] mt-2 h-6"
          />
          <SubmitButton
            onSubmit={async () => { }}
            // buttonStatus={status}
            placeHolder={'reseat'}
            style="text-[10px] bg-white border-[1px]   w-[90px]  text-white rounded-[2px] mt-2 h-6"
          />
        </div>
      </div>
    </div>
  )
}

export default RoomType