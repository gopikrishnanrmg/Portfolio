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
  <header className="fixed inset-x-0 top-0 py-2 md:py-4 w-full z-50 backdrop-blur-md">
    <div className="w-full px-8 flex items-center justify-between">
      <div className="flex items-center">
        <h1 className="text-cyan-500 text-2xl md:text-3xl lg:text-4xl mr-2">GR</h1>
        <div className="w-6 h-6 md:w-8 md:h-8">
          {animationData && <Lottie animationData={animationData} loop autoplay />}
        </div>
      </div>
      <NavBar />
    </div>
  </header>
  )
}

export default Header
