import React, { useState, useEffect } from 'react';
import { createPortal } from 'react-dom';
import ListItem from './ListItem';
import Hamburger from './Hamburger';

const sections = [
  { id: '#hero', title: 'Introduction' },
  { id: '#skills', title: 'Skills' },
  { id: '#work', title: 'Work Experience' },
  { id: '#projects', title: 'Projects' },
  { id: '#testimonials', title: 'Testimonials' },
];

const NavBar = () => {
  const [open, setOpen] = useState(false);
  const [active, setActive] = useState('#hero');

  const toggleMenu = () => setOpen(!open);

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : 'auto';
  }, [open]);

  useEffect(() => {
    const handler = () => {
      const scrollPos = window.scrollY + window.innerHeight / 2; 
      let current = sections[0].id;

      sections.forEach((s) => {
        const el = document.querySelector(s.id);
        if (el) {
          const offsetTop = el.offsetTop;
          const offsetBottom = offsetTop + el.offsetHeight;
          if (scrollPos >= offsetTop && scrollPos < offsetBottom) {
            current = s.id;
          }
        }
      });

      setActive(current);
    };

    window.addEventListener('scroll', handler);
    handler(); 

    return () => window.removeEventListener('scroll', handler);
  }, []);

  return (
    <>
      <nav className='hidden md:flex mr-20'>
        <ul className='flex space-x-12 text-white text-lg'>
          {sections.map((s) => (
            <ListItem key={s.id} id={s.id} title={s.title} active={active} />
          ))}
        </ul>
      </nav>

      <Hamburger open={open} toggle={toggleMenu} />

      {createPortal(
        <div
          className={`fixed inset-0 h-screen backdrop-blur-xl text-white p-6 z-50 transition-all duration-300 ease-in-out
            ${open ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0 pointer-events-none'}`}
        >
          <ul className='flex flex-col gap-6 text-lg mt-20'>
            {sections.map((s) => (
              <ListItem
                key={s.id}
                id={s.id}
                title={s.title}
                active={active}
                onClick={() => setOpen(false)} 
              />
            ))}
          </ul>
        </div>,
        document.body
      )}
    </>
  );
};

export default NavBar;
