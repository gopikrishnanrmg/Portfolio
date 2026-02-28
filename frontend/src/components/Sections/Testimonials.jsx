import React, { useEffect, useState } from 'react'
import { gsap } from 'gsap'
import TestimonialCard from './TestimonialCard'

const Testimonials = () => {
  const [testimonials, setTestimonials] = useState([])
  const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL

  useEffect(() => {
    fetch(`${API_BASE_URL}/portfolio/api/v1/testimonials`)
      .then(res => res.json())
      .then(data => setTestimonials(data))
      .catch(err => console.error('Error fetching testimonials:', err))
  }, [])

  useEffect(() => {
    if (testimonials.length === 0) return;

    const timeout = setTimeout(() => {
      const scrollTriggers = [];
      const animations = [];

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
              toggleActions: 'play none none none',
              once: true,
            },
          }
        );
        animations.push(anim);
      });

      return () => {
        clearTimeout(timeout);
        animations.forEach(anim => {
          anim.scrollTrigger?.kill();
          anim.kill();
        });
        scrollTriggers.forEach(trigger => trigger.kill());
      };
    }, 0);

    return () => clearTimeout(timeout);
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
