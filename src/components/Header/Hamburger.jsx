import { gsap } from 'gsap';
import { useEffect, useRef } from 'react';

const Hamburger = ({ open, toggle }) => {
  const top = useRef(null);
  const mid = useRef(null);
  const bot = useRef(null);

  useEffect(() => {
    if (open) {
      gsap.to(top.current, { rotate: 45, y: 6, duration: 0.3, ease: 'power3.inOut' });
      gsap.to(mid.current, { opacity: 0, duration: 0.2, ease: 'power3.inOut' });
      gsap.to(bot.current, { rotate: -45, y: -6, duration: 0.3, ease: 'power3.inOut' });
    } else {
      gsap.to(top.current, { rotate: 0, y: 0, duration: 0.3, ease: 'power3.inOut' });
      gsap.to(mid.current, { opacity: 1, duration: 0.2, ease: 'power3.inOut' });
      gsap.to(bot.current, { rotate: 0, y: 0, duration: 0.3, ease: 'power3.inOut' });
    }
  }, [open]);

  return (
    <button
      className='md:hidden flex flex-col justify-center items-center w-8 h-8 mr-1 relative z-50'
      onClick={toggle}
    >
      <span ref={top} className='block h-0.5 w-6 bg-white' />
      <span ref={mid} className='block h-0.5 w-6 bg-white my-1' />
      <span ref={bot} className='block h-0.5 w-6 bg-white' />
    </button>
  );
};

export default Hamburger;
