import React, { useEffect, useRef, useState } from 'react'
import SkillComponentMap from './SkillComponentMap'

const SkillsCard = ({ title, techMap }) => {
  const titleRef = useRef(null)
  const cardRef = useRef(null)
  const [rotateX, setRotateX] = useState(0)
  const [rotateY, setRotateY] = useState(0)

  useEffect(() => {
    return () => {};
  }, [])

  const handleMouseMove = e => {
    const rect = cardRef.current?.getBoundingClientRect()
    if (!rect) return
    
    const x = e.clientX - rect.left
    const y = e.clientY - rect.top
    const centerX = rect.width / 2
    const centerY = rect.height / 2

    const newRotateX = ((y - centerY) / centerY) * 10 
    const newRotateY = ((x - centerX) / centerX) * -10

    setRotateX(newRotateX)
    setRotateY(newRotateY)
  }

  const handleMouseLeave = () => {
    setRotateX(0)
    setRotateY(0)
  }

  return (
    <div
      ref={cardRef}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      className='skills-card relative flex flex-col rounded-2xl p-4 w-full min-h-40
                transition-all duration-300 backdrop-blur-2xl bg-black/40 border border-gray-700
                shadow-lg ring-1 ring-white/20 overflow-hidden
                bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]
                hover:border-cyan-500 hover:shadow-cyan-500/30'
      style={{ 
        transformStyle: 'preserve-3d',
        transform: `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`,
        transition: 'transform 0.3s ease-out'
      }}
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
          <SkillComponentMap
            key={item.name}
            name={item.name}
            src={`${item.url}`}
          />
        ))}
      </div>

      <div className='absolute inset-0 bg-gradient-to-br from-black/40 to-cyan-500/10 pointer-events-none' />
    </div>
  )
}

export default SkillsCard
