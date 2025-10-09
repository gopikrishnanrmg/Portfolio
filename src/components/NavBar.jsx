import React from 'react'

const NavBar = () => {
  return (
    <nav className='px-2'>
        <ul className='flex space-x-4 text-white'>
            <li className='hover:text-cyan-200'>Introduction</li>
            <li className='hover:text-cyan-200'>Skills</li>
            <li className='hover:text-cyan-200'>Work Experience</li>
            <li className='hover:text-cyan-200'>Projects</li>
            <li className='hover:text-cyan-200'>Testimonials</li>
        </ul>
    </nav>
  )
}

export default NavBar
