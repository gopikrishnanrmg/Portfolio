import React, { useEffect, useRef, useState } from 'react'
import { gsap } from 'gsap'

const WorkExperienceCard = ({
  role,
  company,
  startDate,
  endDate,
  points = [],
  description,
  note,
}) => {
  const titleRef = useRef(null)
  const noteRef = useRef(null)
  const [expanded, setExpanded] = useState(false)

  useEffect(() => {
    gsap.to(titleRef.current, {
      backgroundPosition: '100% center',
      duration: 4,
      repeat: -1,
      yoyo: true,
      ease: 'linear',
    })

    if (note && noteRef.current) {
      gsap.to(noteRef.current, {
        opacity: 0.6,
        duration: 1.5,
        repeat: -1,
        yoyo: true,
        ease: 'sine.inOut',
      })
    }
  }, [note])

  const visiblePoints = expanded ? points : points.slice(0, 3)

  return (
    <div
      className="experience-card relative flex flex-col rounded-2xl p-6 w-full
           transition-all duration-300 backdrop-blur-2xl bg-black/40 border border-gray-700
           shadow-lg ring-1 ring-white/20 overflow-hidden
           bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]
           hover:border-cyan-500 hover:shadow-cyan-500/30"
    >
      <div
        ref={titleRef}
        className="text-xl font-semibold mb-1 z-10
                   bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                   bg-clip-text text-transparent
                   [background-size:300%_300%] [background-position:0%_center]"
      >
        {role}
      </div>

      {note && (
        <div
          ref={noteRef}
          className="text-sm font-bold text-orange-300 mb-2 z-10 tracking-wide"
        >
          {note}
        </div>
      )}

      <p className="text-sm text-gray-400 z-10">
        {company} • {startDate} - {endDate ? endDate: 'Present'}
      </p>

      {points && points.length > 0 ? (
        <ul className="mt-3 list-disc list-inside text-gray-300 space-y-1 z-10">
          {visiblePoints.map((pt, i) => (
            <li key={i}>{pt}</li>
          ))}
        </ul>
      ) : (
        description && (
          <p className="mt-3 text-gray-300 z-10">{description}</p>
        )
      )}

      {points && points.length > 3 && (
        <button
          onClick={() => setExpanded(!expanded)}
          className="mt-3 text-sm text-cyan-400 hover:underline self-start z-10"
        >
          {expanded ? 'Show less' : 'Show more'}
        </button>
      )}

      <div className="absolute inset-0 bg-gradient-to-br from-black/40 to-cyan-500/10 pointer-events-none" />
    </div>
  )
}

export default React.memo(WorkExperienceCard)