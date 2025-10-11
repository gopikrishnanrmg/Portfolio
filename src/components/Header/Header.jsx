import NavBar from './NavBar'

const Header = () => {
  return (
    <header
      className='fixed flex items-center justify-between py-2 md:py-4 w-full z-70 backdrop-blur-md'
    >
      <h1 className='text-cyan-500 text-4xl mx-5'>GR</h1>
      <NavBar />
    </header>
  )
}

export default Header
