import React, { useEffect, useState } from 'react'
import { gsap } from 'gsap'
import ProjectCard from './ProjectCard'

const Projects = () => {
  const [projects, setProjects] = useState([])
  const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL

  useEffect(() => {
    fetch(`${API_BASE_URL}/portfolio/api/v1/projects`)
      .then(res => res.json())
      .then(data => setProjects(data))
      .catch(err => console.error('Error fetching projects:', err))
  }, [])

  useEffect(() => {
    const animations = []
    const scrollTriggers = []

    gsap.utils.toArray('.project-card').forEach(card => {
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
      '#projects-title',
      { opacity: 0, y: -30 },
      {
        opacity: 1,
        y: 0,
        duration: 1,
        ease: 'power3.out',
        scrollTrigger: {
          trigger: '#projects-title',
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
  }, [projects]) 

  return (
    <section
      id='projects'
      className='relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8'
    >
      <h2
        id='projects-title'
        className='text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400'
      >
        Projects
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-10">
        {projects
          .filter(proj => !proj.hidden)
          .map(proj => (
            <ProjectCard
              key={proj.id}
              title={proj.title}
              description={proj.description}
              tech={proj.tech}
              banner={proj.banner}
              link={proj.link}
            />
          ))}
      </div>
    </section>
  )
}

export default Projects
