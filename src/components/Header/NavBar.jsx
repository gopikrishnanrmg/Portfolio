import React, { useState, useEffect } from 'react'
import ListItem from './ListItem';

const NavBar = () => {

  const [open, setOpen] = useState(false);

  const toggleMenu = () => {
    setOpen(!open)
  }

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : 'auto'
  }, [open])

  return (
    <>
      <nav className='hidden md:flex mr-20'>
        <ul className='flex flex-center space-x-12 text-white text-lg'>
          <ListItem mobile={open} id='#hero' title='Introduction'></ListItem>
          <ListItem mobile={open} id='#hero' title='Skills'></ListItem>
          <ListItem mobile={open} id='#hero' title='Work Experience'></ListItem>
          <ListItem mobile={open} id='#hero' title='Projects'></ListItem>
          <ListItem mobile={open} id='#hero' title='Testimonials'></ListItem>
        </ul>
      </nav>

      <button className='md:hidden text-white text-xl mr-5 z-50' onClick={toggleMenu}>{open ? '✕' : '☰'}</button>
      <div
        className={`fixed inset-0 h-screen backdrop-blur-xl text-white p-6 z-20 transition-all duration-300 ease-in-out
    ${open ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0 pointer-events-none'}`}
      >
        <ul className='flex flex-col gap-6 text-lg mt-20'>
          <ListItem mobile={open} id='#hero' title='Introduction'></ListItem>
          <ListItem mobile={open} id='#hero' title='Skills'></ListItem>
          <ListItem mobile={open} id='#hero' title='Work Experience'></ListItem>
          <ListItem mobile={open} id='#hero' title='Projects'></ListItem>
          <ListItem mobile={open} id='#hero' title='Testimonials'></ListItem>
        </ul>
      </div>

    </>

  )
}

export default NavBar
