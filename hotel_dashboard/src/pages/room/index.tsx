import Header from '../../components/header/header'

import { PencilIcon, XMarkIcon } from '@heroicons/react/16/solid';

import ImageHolder from '../../components/imageHolder';
import RoomsIcon from '../../assets/rooms_icon';
import { enNavLinkType } from '../../module/enNavLinkType';
import { SetStateAction, useContext, useEffect, useRef, useState } from 'react';
import { useToastifiContext } from '../../context/toastifyCustom';
import { enMessage } from '../../module/enMessageType';
import { TextInput } from '../../components/input/textInput';
import SubmitButton from '../../components/button/submitButton';
import { enStatus } from '../../module/enState';
import { iImageHolder } from '../../module/IImageHolder';
import { useMutation, useQuery } from '@tanstack/react-query';
import { enApiType } from '../../module/enApiType';
import apiClient from '../../services/apiClient';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../controller/rootReducer';
import { IRoomType } from '../../module/iRoomType';
import { Guid } from 'guid-typescript';
import { generalMessage } from '../../util/generalPrint';
import { IRoomModule } from '../../module/iRoomModule';
import { enStatsu } from '../../module/enStatsu';
import RoomTable from '../../components/tables/roomTable';
import { IAuthModule } from '../../module/iAuthModule';
import { logout } from '../../controller/redux/jwtSlice';

const Room = () => {
  const refreshToken = useSelector((state: RootState) => state.auth.refreshToken)
  const token = useSelector((state: RootState) => state.auth.token)
  const dispatch = useDispatch()

  const { showToastiFy } = useContext(useToastifiContext)

  const [status, setState] = useState<enStatus>(enStatus.none)
  const [isUpdate, setUpdate] = useState<boolean>(false)
  const [isSingle, setSingle] = useState(false)
  const [isDraggable, changeDraggableStatus] = useState(false)
  const [pageNumber, setPageNumber] = useState(1)


  const [thumnailImage, setThumnail] = useState<iImageHolder>()
  const [images, setImages] = useState<iImageHolder[]>()

  const [roomData, setRoomData] = useState<IRoomModule>({
    roomtypeid: undefined,
    bedNumber: 10,
    capacity: 10,
    pricePerNight: 10,
    status: enStatsu.Available,
    images: undefined,
    roomId: undefined,
    createdAt: undefined,
    beglongTo: undefined
  })

  const [roomHolder, setRoomHover] = useState<IRoomModule | undefined>(undefined)


  const imageRef = useRef<HTMLInputElement>(null);

  const logoutFn = () => {
    dispatch(logout())
  }

  const updateInput = (value: any, key: string) => {

    setRoomData((prev) => ({
      ...prev,
      [key]: value,
    }));

  };

  const changeIsSingleStatus = async (isSingle: boolean) => {
    setSingle(prev => prev = isSingle)
  }

  const selectImage = async (isSingle: boolean) => {
    await changeIsSingleStatus(isSingle).then(() => {
      imageRef.current?.click();
    })
  }

  const uploadImageDisplayFromSelectInput = async (e: React.DragEvent<HTMLDivElement>) => {
    if (imageRef.current && imageRef.current.files && imageRef.current.files[0]) {
      switch (isSingle) {
        case true: {
          const uploadedFile = imageRef.current.files[0];

          const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

          if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
            showToastiFy("You must select a valid image", enMessage.ERROR);
            return;
          }
          
          const fileNew = new File([uploadedFile], "ffffffff." + uploadedFile.type.split('/')[0], { type: uploadedFile.type });

          setThumnail(prev => prev = {
            data: fileNew,
            belongTo: undefined,
            id: undefined,
            isDeleted: false,
            isThumnail: true,
            fileName: undefined
          })

        } break;

        default: {

          let imagesHolder = [] as iImageHolder[];
          for (let i = 0; i < imageRef.current.files.length; i++) {
            const uploadedFile = imageRef.current.files[i];

            const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

            if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
              showToastiFy("You must select a valid image", enMessage.ERROR);
              return;
            }
            imagesHolder.push({
              data: uploadedFile,
              belongTo: undefined,
              id: undefined,
              isDeleted: false,
              isThumnail: false,
              fileName: undefined
            })
          }

          setImages(prev => [...(prev || []), ...imagesHolder]);


        } break;
      }


    }
  };

  const draggbleFun = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    changeDraggableStatus(true)
  }

  const draggableOver = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    changeDraggableStatus(true)
  }

  const handleDragLeave = () => {
    changeDraggableStatus(false);
  };

  const handleDropImage = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    changeDraggableStatus(false)
    if (e.dataTransfer.files) {


      const file = e.dataTransfer.files[0];
      const fileExtension = file.name.split('.').pop()?.toLowerCase();

      if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
        showToastiFy("You must select a valid image", enMessage.ERROR);
        return;
      }
      const fileNew = new File([file], "ffffffff." + file.type.split('/')[0], { type: file.type });
      setThumnail(prev => prev = {
        data: fileNew,

        belongTo: undefined,
        id: undefined,
        isDeleted: false,
        isThumnail: true,
        fileName: undefined

      })
    }

  }

  const handleDropImages = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    changeDraggableStatus(false)
    if (e.dataTransfer.files) {

      for (let i = 0; i < e.dataTransfer.files.length; i++) {
        const uploadedFile = e.dataTransfer.files[i];

        const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

        if (!['png', 'jpg', 'jpeg'].includes(fileExtension || '')) {
          showToastiFy("You must select a valid image", enMessage.ERROR);
          return;
        }

        setImages(prev => [...(prev || []), ...[
          {
            data: uploadedFile,

            belongTo: undefined,
            id: undefined,
            isDeleted: false,
            isThumnail: false,
            fileName: undefined

          }
        ]]);

      }

    }
  }

  const deleteImage = (index: number) => {
    if (!(images === undefined)) {
      setImages(images.filter((_, i) => i !== index))
    }
  }


  const { data: roomtypes, error } = useQuery({
    queryKey: ['roomTypeNotDleted'],

    queryFn: async () => apiClient({
      enType: enApiType.GET,
      endPoint: import.meta.env.VITE_ROOMTYPE + 'true',
      prameters: true,
      isRquireAuth: true,
      jwtRefresh: refreshToken || ""
      , jwtValue: token || ""
    }).then((data) => {
      if (data === undefined) return [];
      const roomType = data.data as IRoomType[];
      updateInput(roomType[0].roomTypeID, 'roomtypeid')
      return roomType;
    })
    ,
  });


  const clearData = () => {
    setRoomData({
      roomtypeid: undefined,
      bedNumber: 0,
      capacity: 0,
      pricePerNight: 0,
      status: enStatsu.Available,
      images: undefined,
      roomId: undefined,
      createdAt: undefined,
      beglongTo: undefined
    })
    setThumnail(undefined)
    setImages(undefined)
  }




  const roomMutaion = useMutation({
    mutationFn: ({ data, endpoint, methodType,
      token, refreshToken
    }: {
      data?: FormData | undefined,
      endpoint: string,
      methodType: enApiType,
      token?: string | null
      refreshToken?: string | null
    }) =>
      apiClient({
        enType: methodType,
        endPoint: endpoint
        , prameters: data,
        isRquireAuth: true,
        isFormData: data != undefined,
        jwtValue: token || "",
        jwtRefresh: refreshToken ?? undefined
      }),
    onSuccess: (data) => {
      setState(enStatus.complate)
      showToastiFy(`user ${isUpdate ? "updated" : "created"} Sueccessfuly`, enMessage.SECCESSFUL);
      clearData()

      if (isUpdate)
        setUpdate(false)

    },
    onError: (error) => {
      setState(enStatus.complate);
      if (error != undefined && error !== null) {
        if (error.status === 401) {
          logoutFn()
        } else {
          showToastiFy(error?.message?.toString() || "An unknown error occurred", enMessage.ERROR)

        }

      }
    }

  })

  const validationInput = () => {

    let message = "";
    if (thumnailImage === undefined) {
      message = "Thumnail is required"
    }
    else if (images === undefined || images.length === 0) {
      message = "Images is required"
    }
    else if (roomData.pricePerNight === 0) {
      message = "Price per night is required";
    }
    else if (roomData.roomtypeid === undefined) {
      message = "Room type is required"
    }
    else if (roomData.capacity === 0) {
      message = "Capacity is required"
    }
    else if (roomData.bedNumber === 0) {
      message = "Bed number is required"
    }
    else if (roomData.roomtypeid === undefined) {
      message = "Room type is required"
      return false;
    }

    if (!(message.length === 0)) {

      showToastiFy(message, enMessage.ATTENSTION)
    }

    return message.length === 0;
  }

  const createOrUpdateRoomType = async () => {
    if (isUpdate === false) {
      const result = !validationInput();

      if (result) return;
    }

    const formData = new FormData();
    if (isUpdate)
      formData.append("roomId", roomData.pricePerNight.toString());

    formData.append("status", `${roomData.status}`);

    formData.append("pricePerNight", `${roomData.pricePerNight}`);

    formData.append("capacity", roomData.capacity.toString());

    formData.append("bedNumber", roomData.bedNumber.toString());

    formData.append("status", roomData.status.toString());

    formData.append("roomtypeid", roomData.roomtypeid?.toString() ?? "");


    if (images && images.length > 0) {
      images.forEach((image, index) => {
        formData.append(`images[${index}].id`, image.id ? image.id.toString() : "");
        formData.append(`images[${index}].belongTo`, image.belongTo ? image.belongTo.toString() : "");
        formData.append(`images[${index}].isDeleted`, image.isDeleted ? image.isDeleted.toString() : "");
        formData.append(`images[${index}].isThumnail`, image.isThumnail ? image.isThumnail.toString() : "");
        if (image.data)
          formData.append(`images[${index}].data`, image.data);
      });

      if (thumnailImage) {

        formData.append(`images[${images.length}].id`, thumnailImage.id ? thumnailImage.id.toString() : "");
        formData.append(`images[${images.length}].belongTo`, thumnailImage.belongTo ? thumnailImage.belongTo.toString() : "");
        formData.append(`images[${images.length}].isDeleted`, thumnailImage.isDeleted ? thumnailImage.isDeleted.toString() : "");
        formData.append(`images[${images.length}].isThumnail`, thumnailImage.isThumnail ? thumnailImage.isThumnail.toString() : "");

        if (thumnailImage.data)
          formData.append(`images[${images.length}].data`, thumnailImage.data);
      }
    }


    await roomMutaion.mutate({
      data: formData,
      endpoint: import.meta.env.VITE_ROOM,
      methodType: isUpdate ? enApiType.PUT : enApiType.POST,
      refreshToken: refreshToken,
      token: token
    });

  }

  const { data, error: roomsError } = useQuery({
    queryKey: ['rooms'],
    queryFn: async () => apiClient({
      enType: enApiType.GET,
      endPoint: import.meta.env.VITE_ROOM + `/${pageNumber}`,
      prameters: undefined,
      isRquireAuth: true,
      jwtValue: token || "",
      jwtRefresh: refreshToken ?? undefined

    }),

  }
  );



  useEffect(() => {
    if (data != undefined) {
      const dataToType = data as unknown as IRoomModule[];
      // generalMessage(JSON.stringify(dataToType))
    }
  }, [data])



  useEffect(() => {
    if (error != undefined && error !== null) {
      if (error.status === 401) {
        dispatch(logout())
      } else {
        showToastiFy(error?.message?.toString() || "An unknown error occurred", enMessage.ERROR)

      }
    }
  }, [error])



  return (
    <div className='flex flex-row'>

      <Header index={3} />

      <div className='min-h-screen w-[calc(100%-192px)] ms-[192px] flex flex-col px-2 items-start  overflow-y-auto '>
        <div className='flex flex-row items-center mt-2'>
          <RoomsIcon className='h-8 fill-black group-hover:fill-gray-200 -ms-1' />
          <h3 className='text-2xl ms-1'>Room</h3>
        </div>

        <div className='w-full h-full mt-4 flex flex-col'>

          <div
            onDrag={draggbleFun}
            onDragOver={draggableOver}
            onDrop={handleDropImage}
            onDragLeave={handleDragLeave}

            className='relative mb-3 md:mb-0 '>

            <input
              multiple={isSingle ? false : true}
              type="file"
              id="file"
              ref={imageRef}
              onChange={uploadImageDisplayFromSelectInput}
              hidden />


            <button
              onClick={() => { selectImage(true) }}
              className='group absolute start-2 top-11 hover:bg-gray-600 hover:rounded-sm '>
              <PencilIcon className='h-6 w-6 border-[1px] border-blue-900 rounded-sm group-hover:fill-gray-200  ' />
            </button>
            <h3 className='text-lg'>
              Thumail
            </h3>
            <div className=' h-44 w-44  flex flex-row justify-center items-center border-[2px] rounded-lg mt-2'
            >
              <ImageHolder
                src={thumnailImage?.data === undefined ? undefined : URL.createObjectURL(thumnailImage.data)}
                style='flex flex-row h-24 w-24 '
                isFromTop={true} />
            </div>
          </div>

          <div className='md:w-3' />


          <div
            onDrag={draggbleFun}
            onDragOver={draggableOver}
            onDrop={handleDropImages}
            onDragLeave={handleDragLeave}

            className=' w-full relative'>
            <button
              onClick={() => { selectImage(false) }}
              className='group absolute start-2 top-11 hover:bg-gray-600 hover:rounded-sm '>
              <PencilIcon className='h-6 w-6 border-[1px] border-blue-900 rounded-sm group-hover:fill-gray-200  ' />
            </button>

            <h3 className='text-lg'>
              Images
            </h3>
            <div className={`min-h-44 w-full flex flex-col md:flex-row  border-[2px] rounded-lg mt-2  md:gap-2 px-2 ${images !== undefined && images.length > 0 && 'pt-9'}  justify-center items-center overflow-scroll pb-2`}
            >
              {
                (images != undefined && images.length > 0) ? images.map((data, index) => {
                  return <div
                    className={`w-full lg:w-20 ${index === 0 ? 'mb-1' : index === images.length - 1 ? '' : 'mb-1'} `}
                    key={index}
                  >
                    <ImageHolder
                      deleteFun={() => { deleteImage(index) }}
                      key={index}
                      typeHolder={enNavLinkType.ROOMS}
                      src={data.data === undefined ? "" : URL.createObjectURL(data.data)}
                      style='flex  w-full lg:w-20 '
                      isFromTop={true} />
                  </div>
                }) : <ImageHolder
                  typeHolder={enNavLinkType.ROOMS}
                  src={undefined}
                  style='flex flex-row h-20 w-20'
                  isFromTop={true} />
              }
            </div>
          </div>

        </div>

        <div className='w-full  md:flex md:row md:flex-row md:flex-wrap md:items-center md:gap-[5px] mt-[10px]'>

          {isUpdate &&
            <div className='w-full md:w-[150px] mb-2'>
              <h3 className='text-[10px]'>Room Status</h3>
              <select name="cars" id="cars" className="w-full md:w-[150px] px-2 py-[4px] rounded-sm border border-gray-300 bg-transparent text-[12px]">
                <option className="bg-transparent hover:bg-transparent" value="Available">Available</option>
                <option className="bg-transparent hover:bg-transparent" value="Booked">Booked</option>
                <option className="bg-transparent hover:bg-transparent" value="Under Maintenance">Under Maintenance</option>
              </select>

            </div>
          }

          <div className='w-full md:w-[150px] mb-2'>
            <TextInput
              keyType='pricePerNight'
              value={roomData.pricePerNight}
              onInput={updateInput}
              placeHolder="Price Per Night"
              style=" w-full md:w-[150px]"
            />
          </div>


          {isUpdate &&
            <div className='w-full md:w-[150px] mb-2'>
              <TextInput
                isDisabled={true}
                type='datetime-local'
                keyType='createdAt'
                value={roomData.createdAt}
                onInput={updateInput}
                placeHolder="Created At"
                style="w-full md:w-[150px]"
              />
            </div>
          }


          <div className='w-full md:w-[150px] '>
            <h3 className=' text-[10px]'>RoomType</h3>
            <select
              onChange={(e) => {
                const selectedID = e.target.value;
                updateInput(selectedID as unknown as Guid, 'roomtypeid')
              }}
              name="cars"
              id="cars"
              className="w-full md:w-[150px]  px-2 py-1 mb-2 rounded-sm border border-gray-300 bg-transparent  text-[12px]">
              {roomtypes !== undefined && (roomtypes).map((roomType, index) => {

                return <option key={index} className="bg-transparent hover:bg-transparent" value={roomType.roomTypeID?.toString()}>{roomType.roomTypeName}</option>
              })}
            </select>
          </div>


          <div className='w-full md:w-[100px] mb-2'>
            <TextInput
              keyType='capacity'
              value={roomData.capacity}
              onInput={updateInput}
              placeHolder="Capacity"
              style=" w-full md:w-[100px]"
            />
          </div>


          <div className='w-full md:w-[100px] mb-2'>
            <TextInput
              keyType='bedNumber'
              value={roomData.bedNumber}
              onInput={updateInput}
              placeHolder="BedNumber"
              style="  w-full md:w-[100px]"
            /></div>

          {isUpdate &&
            <div className='w-full md:w-[150px] mb-2'>
              <TextInput
                isDisabled={true}
                keyType='beglongTo'
                value={roomData.beglongTo}
                onInput={updateInput}
                placeHolder="BelongTo"
                style=" w-full md:w-[150px]"
              />
            </div>
          }
        </div>

        <div className='w-full'>

          <SubmitButton
            onSubmit={async () => createOrUpdateRoomType()}
            buttonStatus={status}
            placeHolder={isUpdate ? 'update' : 'create'}
            style="text-[10px] bg-mainBg   w-full md:w-44 text-white rounded-[4px] my-2 h-8  hover:opacity-90"
            textstyle='text-[14px]'
          />
          <SubmitButton
            // textstyle='text-black'
            // onSubmit={async () => clearData()}
            onSubmit={async () => { }}
            // buttonStatus={status}
            placeHolder={'reseat'}

            style="text-[10px] bg-white border-[1px]  w-full md:w-44 text-white rounded-[4px] my-2 h-8  hover:opacity-90 md:ms-2"
            textstyle='text-[14px] text-black'
          />
        </div>


        <div className='relative overflow-y-scroll'>

          <RoomTable data={data === undefined ? undefined : data.data as unknown as IRoomModule[]}
            setRoomHover={setRoomHover}
            setRoom={setRoomData} seUpdate={function (value: SetStateAction<boolean>): void {
              throw new Error('Function not implemented.');
            }} deleteFunc={function (roomId: Guid): Promise<void> {
              throw new Error('Function not implemented.');
            }} makeRoomVip={function (roomId: Guid): Promise<void> {
              throw new Error('Function not implemented.');
            }} isShwoingDeleted={false} />
        </div>

        {

          <div className={`fixed  bg-gray-600  bottom-14   flex flex-row   h-screen w-screen top-0 start-0  transition-opacity duration-300 ${roomHolder ? 'opacity-100  z-30' : 'opacity-0  -z-10'}`}>
            <button
              onClick={() => {
                setRoomHover(undefined)
              }}
              style={{ cursor: 'pointer' }}
              className='absolute z-30'
            >
              <XMarkIcon className='h-7 w-6 text-white absolute top-0' />

            </button>
            {roomHolder && <div className='absolute h-screen w-screen flex flex-row items-center justify-center z-10'>
              <div>
                <ImageHolder
                  iconColor='text-white'
                  src={`http://172.19.0.1:9000/user/${roomHolder.user.imagePath}`}
                  style='flex flex-row h-20 w-20'
                />
              </div>
              <div className='me-4 ms-4 flex flex-col flex-nowrap max-w-[200px]'>
                <h4 className='text-white'>
                  {roomHolder.user?.personData.name}
                </h4>
                <h4 className='text-white mt-2'>
                  {roomHolder.user?.personData.phone}

                </h4>
                <h4 className='text-white mt-1'>
                  {roomHolder.user?.personData.email}

                </h4>
              </div>
            </div>}
          </div>

        }


      </div>
    </div>
  )
}

export default Room
