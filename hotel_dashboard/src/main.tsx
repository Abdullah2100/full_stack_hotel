import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import UseAuthUser from './context/validLogin'
import App from './App'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <UseAuthUser>
      {<App />}
    </UseAuthUser>
  </StrictMode>,
)
