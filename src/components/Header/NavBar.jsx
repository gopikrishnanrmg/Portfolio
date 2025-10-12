import React, { useState, useEffect } from 'react';
import { createPortal } from 'react-dom';
import ListItem from './ListItem';
import Hamburger from './Hamburger';

const NavBar = () => {
  const [open, setOpen] = useState(false);

  const toggleMenu = () => setOpen(!open);

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : 'auto';
  }, [open]);

  return (
    <>
      <nav className="hidden md:flex mr-20">
        <ul className="flex space-x-12 text-white text-lg">
          <ListItem id="#hero" title="Introduction" />
          <ListItem id="#skills" title="Skills" />
          <ListItem id="#work" title="Work Experience" />
          <ListItem id="#projects" title="Projects" />
          <ListItem id="#testimonials" title="Testimonials" />
        </ul>
      </nav>

      <Hamburger open={open} toggle={toggleMenu} />

      {createPortal(
        <div
          className={`fixed inset-0 h-screen backdrop-blur-xl text-white p-6 z-50 transition-all duration-300 ease-in-out
            ${open ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0 pointer-events-none'}`}
        >
          <ul className="flex flex-col gap-6 text-lg mt-20">
            <ListItem id="#hero" title="Introduction" mobile/>
            <ListItem id="#skills" title="Skills" mobile/>
            <ListItem id="#work" title="Work Experience" mobile/>
            <ListItem id="#projects" title="Projects" mobile/>
            <ListItem id="#testimonials" title="Testimonials" mobile/>
          </ul>
        </div>,
        document.body
      )}
    </>
  );
};

export default NavBar;
