import Header from '../../components/header/header'

import { PencilIcon } from '@heroicons/react/16/solid';

import ImageHolder from '../../components/imageHolder';
import RoomsIcon from '../../assets/rooms_icon';
import { enNavLinkType } from '../../module/enNavLinkType';
import { useContext, useRef, useState } from 'react';
import { useToastifiContext } from '../../context/toastifyCustom';
import { enMessage } from '../../module/enMessageType';

const Room = () => {

  const [isDraggable, changeDraggableStatus] = useState(false)

  const [thumnailImage, setThumnail] = useState<File>()
  const [images, setImages] = useState<File[]>()
  const imageRef = useRef<HTMLInputElement>(null);


  const [isSingle, setSengle] = useState(false)

  const { showToastiFy } = useContext(useToastifiContext)



  const uploadImageDisplayFromSelectInput = async (e) => {
    if (imageRef.current && imageRef.current.files && imageRef.current.files[0]) {
      switch (isSingle) {
        case true: {
          const uploadedFile = imageRef.current.files[0];

          const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

          // Validate file type
          if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
            showToastiFy("You must select a valid image", enMessage.ERROR);
            return;
          }
          setThumnail(uploadedFile)

        } break;

        default: {

          for (let i = 0; i < imageRef.current.files.length; i++) {
            const uploadedFile = imageRef.current.files[i];

            const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

            if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
              showToastiFy("You must select a valid image", enMessage.ERROR);
              return;
            }

            if (images === undefined)
              setImages([uploadedFile])
            else setImages(prev => [...prev, uploadedFile]);  // Correct state update
          }


        } break;
      }


    }
  };



  const draggbleFun = (e) => {
    e.preventDefault();
    changeDraggableStatus(true)
  }

  const draggableOver = (e) => {
    e.preventDefault();
    changeDraggableStatus(true)
  }
  const handleDragLeave = () => {
    changeDraggableStatus(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    changeDraggableStatus(false)
    if (e.dataTransfer.files) {
      switch (isSingle) {
        case true: {
          const file = e.dataTransfer.files[0];
          const fileExtension = file.name.split('.').pop()?.toLowerCase();

          if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
            showToastiFy("You must select a valid image", enMessage.ERROR);
            return;
          }
          setThumnail(file)

        } break;

        default: {

          for (let i = 0; i < e.dataTransfer.files.length; i++) {
            const uploadedFile = e.dataTransfer.files[i];

            const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

            if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
              showToastiFy("You must select a valid image", enMessage.ERROR);
              return;
            }
            if (images === undefined)
              setImages([uploadedFile])
            else setImages(prev => [...prev, uploadedFile]);  // Correct state update
          }
        } break;
      }
    }
  }


  return (
    <div className='flex flex-row'>

      <Header index={3} />

      <div className='min-h-screen w-[calc(100%-192px)] ms-[192px] flex flex-col px-2 items-start  overflow-scroll '>
        <div className='flex flex-row items-center mt-2'>
          <RoomsIcon className='h-8 fill-black group-hover:fill-gray-200 -ms-1' />
          <h3 className='text-2xl ms-1'>Room</h3>
        </div>

        <div className='w-full h-full mt-4 flex flex-col md:flex-row '>

          <div className='relative'>
            <input
              type="file"
              id="file"
              ref={imageRef}
              onChange={uploadImageDisplayFromSelectInput}
              hidden />
            <button
              //onClick={selectImage}
              className='group absolute start-2 top-11 hover:bg-gray-600 hover:rounded-sm '>
              <PencilIcon className='h-6 w-6 border-[1px] border-blue-900 rounded-sm group-hover:fill-gray-200  ' />
            </button>
            <h3 className='text-lg'>
              Thumail
            </h3>
            <div className=' h-44 w-44  flex flex-row justify-center items-center border-[2px] rounded-lg mt-2'
            >
              <ImageHolder
                src={undefined}
                style='flex flex-row h-20 w-20 '
                isFromTop={true} />
            </div>
          </div>

          <div className='md:w-3' />

          <div className=' w-full relative'>
            <button
              //onClick={selectImage}
              className='group absolute start-2 top-11 hover:bg-gray-600 hover:rounded-sm '>
              <PencilIcon className='h-6 w-6 border-[1px] border-blue-900 rounded-sm group-hover:fill-gray-200  ' />
            </button>

            <h3 className='text-lg'>
              Images
            </h3>
            <div className='min-h-44 w-full flex flex-row justify-center items-center border-[2px] rounded-lg mt-2 '
            >
              <ImageHolder
                typeHolder={enNavLinkType.ROOMS}
                src={undefined}
                style='flex flex-row h-20 w-20 '
                isFromTop={true} />
            </div>
          </div>
        </div>


      </div>
    </div>
  )
}

export default Room
