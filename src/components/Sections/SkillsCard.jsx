import React, { useEffect, useRef } from 'react'
import SkillComponentMap from './SkillComponentMap'
import { gsap } from 'gsap'

const SkillsCard = ({ title, techMap }) => {
  const titleRef = useRef(null)
  const cardRef = useRef(null)

  useEffect(() => {
    gsap.to(titleRef.current, {
      backgroundPosition: '200% center',
      duration: 8,
      repeat: -1,
      ease: 'linear',
    })
  }, [])

  const handleMouseMove = e => {
    const rect = cardRef.current.getBoundingClientRect()
    const x = e.clientX - rect.left
    const y = e.clientY - rect.top
    const centerX = rect.width / 2
    const centerY = rect.height / 2

    const rotateX = ((y - centerY) / centerY) * 10 
    const rotateY = ((x - centerX) / centerX) * -10 

    gsap.to(cardRef.current, {
      rotateX,
      rotateY,
      transformPerspective: 1000,
      transformOrigin: 'center',
      duration: 0.4,
      ease: 'power2.out',
    })
  }

  const handleMouseLeave = () => {
    gsap.to(cardRef.current, {
      rotateX: 0,
      rotateY: 0,
      duration: 0.8,
      ease: 'elastic.out(1, 0.3)',
    })
  }

  return (
    <div
      ref={cardRef}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      className='skills-card relative flex flex-col rounded-2xl p-4 w-full min-h-40
                 transition-all duration-300 backdrop-blur-2xl border hover:border-cyan-500
                 hover:shadow-2xl hover:shadow-cyan-500/30 ring-1 ring-white/20 overflow-hidden
                 bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]'
      style={{ transformStyle: 'preserve-3d' }}
    >
      <div
        ref={titleRef}
        className='text-2xl mb-3 z-10
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
