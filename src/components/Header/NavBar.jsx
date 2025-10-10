import React, { useState, useEffect } from 'react'
import ListItem from './ListItem';
import Hamburger from './Hamburger';
import gsap from "gsap";
import { ScrollToPlugin } from "gsap/ScrollToPlugin";

const NavBar = () => {

  const [open, setOpen] = useState(false);

  const toggleMenu = () => {
    setOpen(!open)
  }

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : 'auto'
  }, [open])


gsap.registerPlugin(ScrollToPlugin);

const scrollToSection = (id) => {
  gsap.to(window, { duration: 1, scrollTo: id, ease: "power2.inOut" });
};


  return (
    <>
      <nav className='hidden md:flex mr-20'>
        <ul className='flex flex-center space-x-12 text-white text-lg'>
          <ListItem mobile={open} id='#hero' title='Introduction'></ListItem>
          <ListItem mobile={open} id='#skills' title='Skills'></ListItem>
          <ListItem mobile={open} id='#hero' title='Work Experience'></ListItem>
          <ListItem mobile={open} id='#hero' title='Projects'></ListItem>
          <ListItem mobile={open} id='#hero' title='Testimonials'></ListItem>
        </ul>
      </nav>

      <Hamburger open={open} toggle={toggleMenu} />
      <div
        className={`fixed inset-0 h-screen backdrop-blur-xl text-white p-6 z-20 transition-all duration-300 ease-in-out
    ${open ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0 pointer-events-none'}`}
      >
        <ul className='flex flex-col gap-6 text-lg mt-20'>
          <ListItem mobile={open} id='#hero' title='Introduction'></ListItem>
          <ListItem mobile={open} id='#skills' title='Skills'></ListItem>
          <ListItem mobile={open} id='#hero' title='Work Experience'></ListItem>
          <ListItem mobile={open} id='#hero' title='Projects'></ListItem>
          <ListItem mobile={open} id='#hero' title='Testimonials'></ListItem>
        </ul>
      </div>

    </>

  )
}

export default NavBar
