import React from 'react'
import gsap from 'gsap';
import { ScrollToPlugin } from 'gsap/ScrollToPlugin';

const ListItem = (props) => {

  gsap.registerPlugin(ScrollToPlugin);
  
  const scrollToSection = (id) => {
    gsap.to(window, { duration: 1, scrollTo: id, ease: 'power2.inOut' })
  }

  return (
    <li className={`${props.mobile?'active:text-cyan-500 px-2':'hover:text-cyan-500 rounded-2xl px-2 cursor-pointer'}`}
    onClick={() => scrollToSection(props.id)}>
        {props.title}
    </li>
  )
}

export default ListItem
