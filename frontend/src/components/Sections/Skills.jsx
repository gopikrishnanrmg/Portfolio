import React, { useEffect, useState } from 'react'
import { gsap } from 'gsap'
import SkillsCard from './SkillsCard'

const categories = [
  { key: 'ARCHITECTURE', title: 'Architecture' },
  { key: 'DEVELOPMENT', title: 'Development' },
  { key: 'TESTING', title: 'Testing' },
  { key: 'DEVOPS', title: 'DevOps' },
  { key: 'MISCELLANEOUS', title: 'Additional Exposure (From projects and learning)' }
]

const Skills = () => {
  const [skillsData, setSkillsData] = useState({})
  const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL

  useEffect(() => {
    const fetchSkills = async () => {
      try {
        const results = {}
        for (const cat of categories) {
          const res = await fetch(
            `${API_BASE_URL}/portfolio/api/v1/skills/${cat.key}`
          )
          const data = await res.json()
          results[cat.key] = data
        }
        setSkillsData(results)
      } catch (err) {
        console.error('Error fetching skills:', err)
      }
    }
    fetchSkills()
  }, [])

  useEffect(() => {
    const animations = []
    const scrollTriggers = []

    gsap.utils.toArray('.skills-card').forEach(card => {
      const anim = gsap.fromTo(
        card,
        { opacity: 0, y: 40, scale: 0.95 },
        {
          opacity: 1,
          y: 0,
          scale: 1,
          duration: 0.8,
          ease: 'power3.out',
          scrollTrigger: {
            trigger: card,
            start: 'top 85%',
            toggleActions: 'play none none reverse',
          },
        }
      )
      animations.push(anim)
      if (anim.scrollTrigger) {
        scrollTriggers.push(anim.scrollTrigger)
      }
    })

    const titleAnim = gsap.fromTo(
      '#skills-title',
      { opacity: 0, y: -30 },
      {
        opacity: 1,
        y: 0,
        duration: 1,
        ease: 'power3.out',
        scrollTrigger: {
          trigger: '#skills-title',
          start: 'top 90%',
        },
      }
    )
    animations.push(titleAnim)
    if (titleAnim.scrollTrigger) {
      scrollTriggers.push(titleAnim.scrollTrigger)
    }

    return () => {
      animations.forEach(anim => anim.kill())
      scrollTriggers.forEach(trigger => trigger.kill())
    }
  }, [skillsData])

  return (
    <section
      id='skills'
      className='relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8 overflow-x-hidden'
    >
      <h2
        id='skills-title'
        className='text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400'
      >
        Skills
      </h2>

      <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8'>
        {categories.map(cat => (
          <SkillsCard
            key={cat.key}
            title={cat.title}
            techMap={skillsData[cat.key] || []}
          />
        ))}
      </div>
    </section>
  )
}

export default Skills
