import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import UseAuthUser from './context/validLogin'
import App from './App'
import ToastifyCustom from './context/toastifyCustom'
import { Provider } from 'react-redux'
import store from './controller/store'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
    <ToastifyCustom>
      <UseAuthUser>
        <App />
      </UseAuthUser>
    </ToastifyCustom>
    </Provider>
  </StrictMode>
)
