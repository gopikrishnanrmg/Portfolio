import React, { useRef, useCallback, useEffect, useState } from 'react'
import { bannerMap } from '../../utils/bannerMap'

const ProjectCard = ({ title, description, tech = [], banner, link }) => {
  const cardRef = useRef(null)
  const throttleRef = useRef(null)
  const [lastX, setLastX] = useState(50)
  const [lastY, setLastY] = useState(50)
  const gradientClasses = bannerMap[banner] || ["from-gray-400", "to-gray-600"]

  useEffect(() => {
    let lastUpdate = 0;
    
    const handleMouseMove = (e) => {
      const now = Date.now();
      if (now - lastUpdate < 16) return;
      lastUpdate = now;
      
      if (!cardRef.current) return;
      const rect = cardRef.current.getBoundingClientRect();
      const x = ((e.clientX - rect.left) / rect.width) * 100;
      const y = ((e.clientY - rect.top) / rect.height) * 100;
      
      setLastX(x);
      setLastY(y);
    };

    const handleMouseLeave = () => {
      setLastX(50);
      setLastY(50);
    };

    const node = cardRef.current;
    if (node) {
      node.addEventListener('mousemove', handleMouseMove);
      node.addEventListener('mouseleave', handleMouseLeave);
      
      return () => {
        node.removeEventListener('mousemove', handleMouseMove);
        node.removeEventListener('mouseleave', handleMouseLeave);
      };
    }
  }, []);

  return (
    <div
      ref={cardRef}
      className="project-card relative flex flex-col rounded-2xl overflow-hidden
                  transition-all duration-300 backdrop-blur-2xl bg-black/40 border border-gray-700
                  shadow-lg ring-1 ring-white/20 hover:border-cyan-500
                  hover:shadow-cyan-500/30"
      style={{
        background: `
          radial-gradient(
            250px circle at ${lastX}% ${lastY}%,
            rgba(0,229,255,0.15),
            transparent 80%
          )
        `
      }}
    >
      <div className={`h-28 bg-gradient-to-r ${gradientClasses.join(" ")}`} />

      <div className="p-6 bg-black/40 flex flex-col flex-1 bg-[url(/backgrounds/honeycomb.svg)]">
        <h3
          className="text-lg font-semibold mb-2 z-10
                     bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                     bg-clip-text text-transparent
                     [background-size:300%_300%] [background-position:0%_center]"
        >
          {title}
        </h3>

        <p className="text-gray-300 text-sm mb-3 z-10 flex-1">{description}</p>

        {tech.length > 0 && (
          <ul className="flex flex-wrap gap-2 text-xs text-cyan-300 z-10 mb-4">
            {tech.map((t, i) => (
              <li
                key={i}
                className="px-2 py-1 rounded-md border border-cyan-500/40 bg-cyan-500/10"
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

export default React.memo(ProjectCard)

