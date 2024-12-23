import React from 'react'
import Header from '../../components/header/header'

const User = () => {
  return (
    <div className='flex flex-row'>
        <Header index={1} />
        <div className='h-screen w-full flex flex-col px-2'>
            <button
            className='bg-blue-600 w-20 text-white rounded-sm mt-10'
            >add</button>

        </div>
      
    </div>
  )
}

export default User