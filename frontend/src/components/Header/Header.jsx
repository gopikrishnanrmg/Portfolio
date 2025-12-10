import Lottie from 'lottie-react'
import NavBar from './NavBar'
import { useEffect, useState } from 'react'

const Header = () => {

  const [animationData, setAnimationData] = useState(null)

  useEffect(() => {
    fetch('/animations/welcome_robo.json')
      .then(res => res.json())
      .then(data => setAnimationData(data))
  }, [])

  return (
    <header className='fixed inset-x-0 top-0 py-2 md:py-4 w-full z-50 backdrop-blur-md'>
      <div className='mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] px-8 flex items-center justify-between'>
        <div className='flex flex-center'>
          <h1 className='text-cyan-500 text-3xl md:text-4xl lg:text-5xl ml-1 mr-2'>GR</h1>
          <div className="w-6 h-6 md:w-6 md:h-6">
            <Lottie animationData={animationData} loop autoplay />
          </div>
        </div>
        <NavBar />
      </div>
    </header>
  )
}

export default Header
