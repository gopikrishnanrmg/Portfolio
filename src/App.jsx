import { useState } from 'react'
import './App.css'
import Header from './components/Sections/Header/Header'
import Hero from './components/Sections/Hero'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className='min-h-screen flex flex-col'>
    <Header/>
    <main>
    <Hero></Hero>
    </main>
    </div>
  )
}

export default App
