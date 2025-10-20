import React, { useRef, useEffect } from 'react'
import { gsap } from 'gsap'

const ProjectCard = ({ title, description, tech = [], banner, link }) => {
  const titleRef = useRef(null)

  useEffect(() => {
    gsap.to(titleRef.current, {
      backgroundPosition: '100% center',
      duration: 4,
      repeat: -1,
      yoyo: true,
      ease: 'linear',
    })
  }, [])

  return (
    <div
      className='project-card relative flex flex-col rounded-2xl overflow-hidden
                 transition-all duration-300 backdrop-blur-2xl border border-gray-700
                 shadow-lg ring-1 ring-white/20'
    >
      <div className={`h-28 bg-gradient-to-r ${banner}`} />

      <div className='p-6 bg-black/40 flex flex-col flex-1'>
        <h3
          ref={titleRef}
          className='text-lg font-semibold mb-2 z-10
                     bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                     bg-clip-text text-transparent
                     [background-size:300%_300%] [background-position:0%_center]'
        >
          {title}
        </h3>

        <p className='text-gray-300 text-sm mb-3 z-10 flex-1'>{description}</p>

        {tech.length > 0 && (
          <ul className='flex flex-wrap gap-2 text-xs text-cyan-300 z-10 mb-4'>
            {tech.map((t, i) => (
              <li
                key={i}
                className='px-2 py-1 rounded-md border border-cyan-500/40 bg-cyan-500/10'
              >
                {t}
              </li>
            ))}
          </ul>
        )}

        {link && link.trim() !== '' ? (
          <a
            href={link}
            target="_blank"
            rel="noopener noreferrer"
            className="mt-auto inline-block text-sm text-cyan-400 hover:underline"
          >
            View Project →
          </a>
        ) : (
          <span className="mt-auto inline-block text-sm text-gray-500 italic">
            Proprietary/No link available
          </span>
        )}

      </div>
    </div>
  )
}

export default ProjectCard
