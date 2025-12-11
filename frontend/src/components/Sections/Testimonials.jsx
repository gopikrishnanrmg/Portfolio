import React, { useEffect } from 'react'
import { gsap } from 'gsap'
import TestimonialCard from './TestimonialCard'

const testimonials = [
    {
        id: 'sv-1',
        name: 'Dr. Venkataraman Sarma',
        role: 'CTO, Ensurity Technologies',
        text: 'He suggested AI-based methods to improve consensus fairness — a calm yet confident leader with innovative yet systematic thinking.',
        initials: 'SV',
        accent: 'from-purple-400 to-cyan-500',
    },
    {
        id: 'ck-1',
        name: 'Mr. Chakradhar K',
        role: 'Director, Ensurity Technologies',
        text: 'He played a vital role in our Blockchain Token Authentication project — consistently exceeding expectations in security, identity verification, and decentralized wallet communication.',
        initials: 'CK',
        accent: 'from-pink-400 to-purple-500',
    },
    {
        id: 'ms-1',
        name: 'Dr. M Sethumadhavan',
        role: 'Professor and Head of Cybersecurity, Amrita Vishwa Vidyapeetham',
        text: 'Even as a associate developer, he delivered a well-documented, efficient implementation using recursion and object-oriented design — and went further by building a UI without being asked. A true perfectionist in software delivery.',
        initials: 'MS',
        accent: 'from-cyan-400 to-pink-500',
    },
    {
        id: 'f-1',
        name: 'filjusz',
        role: 'Senior Member, XDA Developers',
        text: 'Amazing project! Thanks for your work, really appreciate it. Glad to have opportunity to enjoy it. I hope it will grow with time.',
        initials: 'F',
        accent: 'from-cyan-400 to-pink-500',
    }
]

const Testimonials = () => {
    useEffect(() => {
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
                        toggleActions: 'play none none reverse',
                    },
                }
            )
            animations.push(anim);
            if (anim.scrollTrigger) {
                scrollTriggers.push(anim.scrollTrigger);
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
            animations.forEach(anim => anim.kill());
            nameAnim.kill();
            scrollTriggers.forEach(trigger => trigger.kill());
        };
    }, [])

    return (
        <section id="testimonials" className="relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8">
            <h2
                id="testimonials-title"
                className="text-3xl md:text-4xl lg:text-5xl font-extralight text-center mb-16 text-cyan-400"
            >
                Testimonials
            </h2>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 items-stretch">
                {testimonials.map((t) => (
                    <TestimonialCard key={t.id} {...t} />
                ))}
            </div>

        </section>
    )
}

export default Testimonials

