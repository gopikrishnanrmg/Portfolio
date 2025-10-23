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
    <header
      className='fixed flex items-center justify-between py-2 md:py-4 w-full z-50 backdrop-blur-md'
    >
      <div className='flex flex-center'>
        <h1 className='text-cyan-500 text-4xl ml-5 mr-1'>GR</h1>
        <div className="w-6 h-6 md:w-6 md:h-6">
          <Lottie animationData={animationData} loop autoplay />
        </div>
      </div>
      <NavBar />
    </header>
  )
}

export default Header
