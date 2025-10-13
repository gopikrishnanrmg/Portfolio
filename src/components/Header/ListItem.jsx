import React from 'react'
import gsap from 'gsap';
import { ScrollToPlugin } from 'gsap/ScrollToPlugin';

gsap.registerPlugin(ScrollToPlugin);

const ListItem = ({ id, title, active }) => {
  const scrollToSection = (id) => {
    gsap.to(window, { duration: 1, scrollTo: id, ease: 'power2.inOut' })
  }

  const isActive = active === id;
  
  return (
 <li
  className={`px-2 cursor-pointer rounded-2xl transition-colors 
    ${isActive ? 'text-cyan-400 font-bold' : 'text-white'} 
    hover:text-cyan-500`}
  onClick={() => scrollToSection(id)}
>
  {title}
</li>
  )
}

export default ListItem
