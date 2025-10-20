import React, { useEffect, useRef, useState } from 'react'
import { gsap } from 'gsap'

const WorkExperienceCard = ({
  role,
  company,
  period,
  points = [],
  description,
  highlightPrompt = false,
}) => {
  const titleRef = useRef(null)
  const subtitleRef = useRef(null)
  const [expanded, setExpanded] = useState(false)

  useEffect(() => {
    gsap.to(titleRef.current, {
      backgroundPosition: '100% center',
      duration: 4,
      repeat: -1,
      yoyo: true,
      ease: 'linear',
    })

    if (highlightPrompt && subtitleRef.current) {
      gsap.to(subtitleRef.current, {
        opacity: 0.6,
        duration: 1.5,
        repeat: -1,
        yoyo: true,
        ease: 'sine.inOut',
      })
    }
  }, [highlightPrompt])

  const visiblePoints = expanded ? points : points.slice(0, 3)

  return (
    <div
      className='experience-card relative flex flex-col rounded-2xl p-6 w-full
                 transition-all duration-300 backdrop-blur-2xl border border-cyan-500
                 shadow-2xl shadow-cyan-500/30 ring-1 ring-white/20 overflow-hidden
                 bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]'
    >
      <div
        ref={titleRef}
        className='text-xl font-semibold mb-1 z-10
                   bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                   bg-clip-text text-transparent
                   [background-size:300%_300%] [background-position:0%_center]'
      >
        {role}
      </div>

      {highlightPrompt && (
        <div
          ref={subtitleRef}
          className='text-sm font-bold text-orange-300 mb-2 z-10 tracking-wide'
        >
          &#9888; Please read the description as the role was primarily focused in development and automation as I was a member of the automation team↓
        </div>
      )}

      <p className='text-sm text-gray-400 z-10'>
        {company} • {period}
      </p>

      {points && points.length > 0 ? (
        <ul className='mt-3 list-disc list-inside text-gray-300 space-y-1 z-10'>
          {visiblePoints.map((pt, i) => (
            <li key={i}>{pt}</li>
          ))}
        </ul>
      ) : (
        description && (
          <p className='mt-3 text-gray-300 z-10'>{description}</p>
        )
      )}

      {points && points.length > 3 && (
        <button
          onClick={() => setExpanded(!expanded)}
          className='mt-3 text-sm text-cyan-400 hover:underline self-start z-10'
        >
          {expanded ? 'Show less' : 'Show more'}
        </button>
      )}

      <div className='absolute inset-0 bg-gradient-to-br from-black/40 to-cyan-500/10 pointer-events-none' />
    </div>
  )
}

export default WorkExperienceCard