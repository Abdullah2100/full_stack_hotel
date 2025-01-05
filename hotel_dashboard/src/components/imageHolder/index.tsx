import {  UserCircleIcon } from '@heroicons/react/16/solid'
import React, { useState } from 'react';

interface ImageHolderProps {
  src?: string | undefined;
  style?: string | undefined;
}

const ImageHolder = ({ src, style }: ImageHolderProps) => {
  const [isHasError, setHasError] = useState<boolean>(false);

  const handleError = () => {
    setHasError(true);
  };

  const imageHandler = () => {
    console.log(`\n\nthis the is has error  ${(isHasError || src === undefined)}\n ${src}\n\n`);
    return (isHasError || src === undefined) ? <UserCircleIcon /> : <img
      className=''
      src={src} onError={handleError} />;
  };

  return (
    <div className={style}>
      {imageHandler()}
    </div>
  );
};

export default ImageHolder;
