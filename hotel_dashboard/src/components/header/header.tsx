import React from 'react'
import { Link } from 'react-router-dom'
import NavLink from '../navLink/navLink'
import Logo from '../../assets/logo'

interface iHeaderType {
    index?: number | undefined
}
const Header = ({ index = 0 }: iHeaderType) => {
    return (
        <div className='h-screen w-48 bg-mainBg flex flex-col items-center  pe-7'>

            {/* logo         */}
            <div className='mt-4'>
                <Link to={'/'}>
                    {/* <img src={Logo} className='h-8 w-8 stroke-white'/> */}
                    <Logo className='h-12 text-green-50' />
                </Link>
            </div>

            {/* nav */}
            <NavLink navTo={'/users'} name={' المستخدمين'} isCurrentIndex={index===1} />



        </div>
    )
}

export default Header