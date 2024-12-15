import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import NotFoundPage from './pages/NotFound/notfound'
import UseAuthUser from './context/validLogin'
import App from './App'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <UseAuthUser>
      {<App />}
    </UseAuthUser>
  </StrictMode>,
)
