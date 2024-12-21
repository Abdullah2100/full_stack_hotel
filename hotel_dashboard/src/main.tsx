import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import UseAuthUser from './context/validLogin'
import App from './App'
import ToastifyCustom from './context/toastifyCustom'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ToastifyCustom>
      <UseAuthUser>
        <App />
      </UseAuthUser>
    </ToastifyCustom>
  </StrictMode>
)
