import React, { useEffect, useRef, useState } from 'react'
import { gsap } from 'gsap'
import WorkExperienceCard from './WorkExperienceCard'

const WorkExperience = () => {
  const [experiences, setExperiences] = useState([])
  const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL

  const sectionRef = useRef(null)
  const lineRef = useRef(null)

  useEffect(() => {
    fetch(`${API_BASE_URL}/portfolio/api/v1/workexps`)
      .then(res => res.json())
      .then(data => setExperiences(data))
      .catch(err => console.error('Error fetching experiences:', err))
  }, [])

  useEffect(() => {
    const animations = []
    const scrollTriggers = []

    const lineAnim = gsap.fromTo(
      lineRef.current,
      { scaleY: 0, transformOrigin: 'top center' },
      {
        scaleY: 1,
        ease: 'none',
        scrollTrigger: {
          trigger: sectionRef.current,
          start: 'top center',
          end: 'bottom center',
          scrub: 1,
        },
      }
    )
    animations.push(lineAnim)
    if (lineAnim.scrollTrigger) scrollTriggers.push(lineAnim.scrollTrigger)

    gsap.utils.toArray('.experience-card').forEach(card => {
      const anim = gsap.fromTo(
        card,
        { opacity: 0, y: 50 },
        {
          opacity: 1,
          y: 0,
          duration: 0.8,
          ease: 'power3.out',
          scrollTrigger: {
            trigger: card,
            start: 'top 80%',
            toggleActions: 'play none none reverse',
          },
        }
      )
      animations.push(anim)
      if (anim.scrollTrigger) scrollTriggers.push(anim.scrollTrigger)
    })

    return () => {
      animations.forEach(anim => anim.kill())
      scrollTriggers.forEach(trigger => trigger.kill())
    }
  }, [experiences])

  return (
    <section
      id="work-experience"
      ref={sectionRef}
      className="relative mx-auto w-full max-w-5xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8"
    >
      <h2 className="text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400">
        Work Experience
      </h2>

      <div className="relative">
        <div
          ref={lineRef}
          className="absolute left-1/2 -translate-x-1/2 top-0 w-1 h-full rounded-full
             bg-gradient-to-b from-cyan-400 via-purple-500 to-emerald-500
             will-change-transform"
        />

        <div className="flex flex-col space-y-16 relative">
          {experiences.slice().map((exp, i) => (
            <div
              key={exp.workExpId}
              className={`relative flex w-full ${
                i % 2 === 0 ? 'md:justify-start' : 'md:justify-end'
              }`}
            >
              <div
                className={`w-full md:w-[48%] ${
                  i % 2 === 0 ? 'md:mr-8' : 'md:ml-8'
                }`}
              >
                <WorkExperienceCard {...exp} />
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default WorkExperience
