import React, { useEffect, useRef } from 'react'
import SkillComponentMap from './SkillComponentMap'
import { gsap } from 'gsap'

const SkillsCard = ({ title, techMap }) => {
  const titleRef = useRef(null)

  useEffect(() => {
    gsap.to(titleRef.current, {
      backgroundPosition: '200% center',
      duration: 8,
      repeat: -1,
      ease: 'linear',
    })
  }, [])

  return (
    <div
      className='skills-card relative flex flex-col rounded-2xl p-4 w-full min-h-40
                 transition-all duration-300 backdrop-blur-2xl border border-cyan-500
                 shadow-2xl shadow-cyan-500/30 ring-1 ring-white/20 overflow-hidden
                 bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]'
    >
      <div
        ref={titleRef}
        className='text-2xl font-light mb-3 z-10
                   bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                   bg-clip-text text-transparent
                   [background-size:200%_200%]'
      >
        {title}
      </div>

      <div className='flex flex-wrap gap-2 z-10'>
        {techMap.map(item => (
          <SkillComponentMap key={item.name} name={item.name} src={item.src} />
        ))}
      </div>

      <div className='absolute inset-0 bg-gradient-to-br from-black/40 to-cyan-500/10 pointer-events-none' />
    </div>
  )
}

export default SkillsCard
