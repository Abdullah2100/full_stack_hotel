import { UserCircleIcon, XMarkIcon } from '@heroicons/react/16/solid'
import React, { useState } from 'react';
import { generalMessage } from '../../util/generalPrint';
import { enNavLinkType } from '../../module/enNavLinkType';
import { PhotoIcon } from '@heroicons/react/24/outline';

interface ImageHolderProps {
  src?: string | undefined;
  style?: string | undefined;
  iconColor?:string |undefined;
  isFromTop?: boolean | undefined;
  typeHolder?: enNavLinkType | undefined;
  deleteFun?: () => void | undefined;
}

const ImageHolder = ({
  src,
  style,
  iconColor,
  isFromTop = false,
  typeHolder = undefined,
  deleteFun = undefined
}: ImageHolderProps) => {

  const [isHasError, setHasError] = useState<boolean>(false);

  const handleError = (e) => {
    setHasError(true);
  };

  const handleNotFoundIconHolder = () => {

    switch (typeHolder) {

      case enNavLinkType.ROOMS: return <PhotoIcon />
      default: return <UserCircleIcon className={`${iconColor ? 'text-white' : ''}`} />
    }

  }
  const imageHandler = () => {

    return (src === undefined || isHasError) ? handleNotFoundIconHolder() : <img
      
      src={src} onError={handleError} />;
  };

  return (
    <div className={`${style} relative`}>
      {
        deleteFun &&
        <button onClick={deleteFun}>
          <XMarkIcon 
          className={`absolute -top-1 -end-1 h-6 w-6 'text-gray-400'}`} />
        </button>
      }
      {imageHandler()}
    </div>
  );
};

export default ImageHolder;
