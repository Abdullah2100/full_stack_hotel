import 'react'
import './style.css';
import notFoundImage from '../../assets/page-not-found-removebg-preview.png'

const NotFoundImage = ()=>{
return (
    <>
       <img src={notFoundImage} className={"notFoundImage"}/>
    </>
)
}

export default NotFoundImage;