import {  UserCircleIcon } from '@heroicons/react/16/solid'
import React, { useState } from 'react';
import { generalMessage } from '../../util/generalPrint';

interface ImageHolderProps {
  src?: string | undefined;
  style?: string | undefined;
  isFromTop?:boolean | undefined
}

const ImageHolder = ({ src, style,isFromTop=false }: ImageHolderProps) => {
  
  // generalMessage(`this shown the src data ${src}`)
  
  const [isHasError, setHasError] = useState<boolean>(false);

  const handleError = (e) => {
     generalMessage(`this error ${JSON.stringify(e)}`,true)
    setHasError(true);
  };

  const imageHandler = () => {
    return (src === undefined||isHasError ) ? <UserCircleIcon  /> : <img
      className=''
      src={src} onError={handleError} />;
  };

  return (
    <div className={isHasError?undefined:style}>
      {imageHandler()}
    </div>
  );
};

export default ImageHolder;
