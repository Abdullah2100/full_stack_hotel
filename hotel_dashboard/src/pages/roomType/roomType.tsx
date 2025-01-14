import { useContext, useEffect, useRef, useState } from 'react';
import Header from '../../components/header/header'
import { TextInput } from '../../components/input/textInput'
import { IRoomType } from '../../module/roomModule';
import { PencilIcon, RectangleGroupIcon } from '@heroicons/react/16/solid';
import SubmitButton from '../../components/button/submitButton';
import { enMessage } from '../../module/enMessageType';
import { useToastifiContext } from '../../context/toastifyCustom';
import { enStatus } from '../../module/enState';
import apiClient from '../../services/apiClient';
import { enApiType } from '../../module/enApiType';
import { notifyManager, useMutation, useQuery } from '@tanstack/react-query';
import { RootState } from '../../controller/rootReducer';
import { useSelector } from 'react-redux';
import RoomTypeTable from '../../components/tables/roomTypeTable';
import ImageHolder from '../../components/imageHolder';
import { generalMessage } from '../../util/generalPrint';

const RoomType = () => {
  const refreshToken = useSelector((state: RootState) => state.auth.refreshToken)

  const { showToastiFy } = useContext(useToastifiContext)
  const [status, setState] = useState<enStatus>(enStatus.none)

  const [isUpdate, setUpdate] = useState<boolean>(false)
  const imageRef = useRef<HTMLInputElement>(null);
  const [imageHolder, setImageHolder] = useState<File | undefined>(undefined);
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

      // Validate file type
      if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
        showToastiFy("You must select a valid image", enMessage.ERROR);
        return;
      }

      const cachedURL = URL.createObjectURL(uploadedFile);

      const img = new Image();
      img.onload = () => {
        const { naturalHeight, naturalWidth } = img;

        showToastiFy(`Image height: ${naturalHeight}\nImage width: ${naturalWidth}`, enMessage.ERROR);

        //   if (naturalHeight > 40 || naturalWidth > 40) {
        //     showToastiFy("Image must have a minimum height and width of 40px", enMessage.ERROR);
        //     return;
        //   }

        setImage(cachedURL);
        setImageHolder(uploadedFile)
      };

      img.src = cachedURL;
    }
  };


  const clearData = () => {
    setRoomType({
      roomTypeName: '',
      createdAt: null,
      createdBy: null,
      roomTypeID: null
    })
    setImage(undefined)
    setImageHolder(undefined)
  }


  const { data, error, refetch } = useQuery({

    queryKey: ['users'],
    queryFn: async () => apiClient({
      enType: enApiType.GET,
      endPoint: import.meta.env.VITE_ROOMTYPES,
      prameters: undefined,
      isRquireAuth: true,
      jwtValue: refreshToken || ""
    }),
  }
  );

  const userMutaion = useMutation({
    mutationFn: ({ data, endpoint, methodType,
      jwtToken
    }: {
      data?: FormData | undefined,
      endpoint: string,
      methodType: enApiType,
      jwtToken?: string | null
    }) =>
      apiClient({
        enType: methodType,
        endPoint: endpoint
        , prameters: data,
        isRquireAuth: true,
        jwtValue: jwtToken ?? undefined,
        isFormData: data != undefined
      }),
    onSuccess: (data) => {
      setState(enStatus.complate)
      showToastiFy(`user ${isUpdate ? "updated" : "created"} Sueccessfuly`, enMessage.SECCESSFUL);
      clearData()
      setImage(undefined)
      if (isUpdate)
        setUpdate(false)
      refetch();
    },
    onError: (error) => {
      setState(enStatus.complate);
      showToastiFy(error.message, enMessage.ERROR);

    }

  })

  const validationInput = () => {
    let message = "";
    if (roomType.roomTypeName.length < 1) {
      message = "name must not be empty";
    }
    else if (image === undefined) {
      message = "image must not be null";
    }
    if (message.length > 0)
      showToastiFy(message, enMessage.ATTENSTION)
    return message.length > 0
  }

  const createOrUpdateRoomType = () => {
    if (!isUpdate) {
      if (validationInput()) {
        return;
      }
    }
    const roomtTypeData = new FormData();

    if (roomType.roomTypeName.length > 0)
      roomtTypeData.append("name", roomType.roomTypeName)

    if (imageHolder !== undefined)
      roomtTypeData.append("image", imageHolder)
    if(isUpdate)
      roomtTypeData.append("id",roomType.roomTypeID) 

    let endPoint = import.meta.env.VITE_CREATEROOMTYPE;
    const method= isUpdate ? enApiType.PUT : enApiType.POST;
    generalMessage(`this the method  from ${method}`)

    userMutaion.mutate({
      data: roomtTypeData,
      endpoint: endPoint,
      methodType: method,
      jwtToken: refreshToken
    })
  }


  useEffect(() => {
    if (error != undefined) {
      showToastiFy(error?.message?.toString() || "An unknown error occurred", enMessage.ERROR)
    }
  }, [error])


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
              // onLoad={uploadImageDisplay}
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
              <div className='absolute h-full w-full  flex flex-row justify-center items-center '
              >
                <ImageHolder
                  src={image ?? roomType?.imagePath ?
                    `http://172.19.0.1:9000/roomtype/` + roomType.imagePath?.toString() : undefined}
                  style='flex flex-row h-20 w-20 '
                  isFromTop={true} />
              </div>

            </div>

          </div>


        </div>
        <div className={'flex flex-row gap-3 mb-2'}>
          <SubmitButton
            onSubmit={async () => createOrUpdateRoomType()}
            // buttonStatus={status}
            placeHolder={isUpdate ? 'update' : 'create'}
            style="text-[10px] bg-mainBg   w-[90px]  text-white rounded-[2px] mt-2 h-6"
          />
          <SubmitButton
            textstyle='text-black'
            onSubmit={async () => clearData()}
            // buttonStatus={status}
            placeHolder={'reseat'}
            style="text-[10px] bg-white border-[1px]   w-[90px]  text-white rounded-[2px] mt-2 h-6"
          />
        </div>
        <RoomTypeTable
          data={data !== undefined ? data.data as unknown as IRoomType[] : undefined}
          setRoomType={setRoomType}
          setUpdate={setUpdate}
          // deleteFunc={()=>{} } 
          isShwoingDeleted={false} />
      </div>
    </div>
  )
}

export default RoomType