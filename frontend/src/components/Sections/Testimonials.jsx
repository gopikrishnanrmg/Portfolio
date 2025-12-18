import React, { useEffect, useState } from 'react'
import { gsap } from 'gsap'
import TestimonialCard from './TestimonialCard'

const Testimonials = () => {
  const [testimonials, setTestimonials] = useState([])

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_BASE_URL}/portfolio/api/v1/testimonials`)
      .then(res => res.json())
      .then(data => setTestimonials(data))
      .catch(err => console.error('Error fetching testimonials:', err))
  }, [])

  useEffect(() => {
    const scrollTriggers = []
    const animations = []

    gsap.utils.toArray('.testimonial-card').forEach(card => {
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

    const nameAnim = gsap.to('.testimonial-name', {
      backgroundPosition: '100% center',
      duration: 4,
      repeat: -1,
      yoyo: true,
      ease: 'linear',
    })

    return () => {
      animations.forEach(anim => anim.kill())
      nameAnim.kill()
      scrollTriggers.forEach(trigger => trigger.kill())
    }
  }, [testimonials]) 

  return (
    <section
      id="testimonials"
      className="relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8"
    >
      <h2
        id="testimonials-title"
        className="text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400"
      >
        Testimonials
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 items-stretch">
        {testimonials.map(t => (
          <TestimonialCard
            key={t.testimonialId}
            name={t.name}
            role={t.role}
            text={t.text}
            initials={t.initials}
            accent={t.accent}
          />
        ))}
      </div>
    </section>
  )
}

export default Testimonials
