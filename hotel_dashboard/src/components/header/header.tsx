import React from 'react'
import { Link } from 'react-router-dom'
import NavLink from '../navLink/navLink'
import Logo from '../../assets/logo'
import { enNavLinkType } from '../../module/enNavLinkType'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../controller/rootReducer'
import { logout } from '../../controller/redux/jwtSlice'
import Logout from '../button/logoutButton'

interface iHeaderProp {
    index?: number | undefined
}
const Header = ({ index = 0 }: iHeaderProp) => {
    return (
        <div className="w-48 bg-mainBg flex flex-col items-center pe-7 fixed top-0 left-0 h-full overflow-hidden">
        {/* logo */}
        <div className="mt-4">
          <Link to={'/'}>
            {/* <img src={Logo} className='h-8 w-8 stroke-white'/> */}
            <Logo className="h-12 text-green-50" />
          </Link>
        </div>
  
        {/* nav */}
        <NavLink navTo={'/users'} name={' المستخدمين'} isCurrentIndex={index === 1}  navType={enNavLinkType.ROOM}/>
        <Logout   />
      </div>
    )
}

export default Header
