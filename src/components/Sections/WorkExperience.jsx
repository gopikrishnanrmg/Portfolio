import React, { useEffect, useRef } from 'react'
import { gsap } from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'
import WorkExperienceCard from './WorkExperienceCard'

gsap.registerPlugin(ScrollTrigger)

const experiences = [
  {
    role: 'Performance Test Engineer',
    company: 'SOTI',
    period: 'Sep 2025 – Present',
    points: [
      'Architect and lead developer of a multithreaded VM orchestration solution (Java, Swing, SQL Server, OpenJPA, PowerShell).',
      'Modeled architecture with UML, applied design patterns, automated operations with CI/CD, JUnit, Logging, Maven.',
      'Designed and implemented a VM usage notification service (Java + Power Automate) reducing cloud costs by 25%.',
      'Secured web application endpoints by configuring a reverse proxy.',
    ],
    highlightPrompt: true, 
  },
  {
    role: 'Quality Assurance Specialist',
    company: 'SOTI',
    period: 'Sep 2023 – Aug 2025',
    points: [
      'Developed a Java library with custom JMeter plugins, AI chatbot integration, InfluxDB push, CI/CD, JUnit, Logging, Maven.',
      'Created a web-based AWS stats dashboard (HTML, CSS, JS, AWS APIs) reducing infra cost by 15%.',
      'Used Docker for debugging and containerization POCs.',
      'Maintained internal legacy Java libraries for automation scripts.',
      'Implemented system stats visualization during tests with Grafana.',
      'Built automation scripts (Python, JMeter JDBC, Groovy, Selenium, JS) for API performance testing.',
      'Automated DevOps pipelines on Jenkins with PowerShell.',
      'Developed a browser extension (JavaScript + Jenkins APIs) to assist the team.',
      'Worked on AI POCs for JMeter script generation using internal LLM + MongoDB + ChromaDB in Python.',
    ],
    highlightPrompt: true, 
  },
  {
    role: 'Blockchain Engineer',
    company: 'Ensurity Technologies',
    period: 'Jun 2019 – Oct 2021',
    points: [
      'Developed REST APIs for Rubix Blockchain wallet (Spring Boot).',
      'Implemented Gossip Protocol (Java + IPFS).',
      'Built consensus protocol with a team of 6.',
      'Developed Decentralized Identity Android app (Java).',
      'Created automation tool for blockchain testing (Java + Bash).',
      'Containerized Rubix wallet with Docker.',
      'Built Flask REST API server (Python) for quorum node allocation.',
      'POCs on MiniKube (Kubernetes) for orchestrating transactions.',
      'Created Android POC for XSense Passwordless Authentication.',
      'Researched and developed IPFS mobile Android solution (Java + IPFS Linux binary).',
    ],
  },
  {
    role: 'Blockchain Intern',
    company: 'Ensurity Technologies',
    period: 'Jan 2019 – May 2019',
    points: [
      'Built Node.js REST API server for issuing KYC tokens.',
      'Devised Rubix Blockchain’s Non-Linear Secret Sharing scheme (Java).',
    ],
  },
]

const WorkExperience = () => {
  const sectionRef = useRef(null)
  const lineRef = useRef(null)

  useEffect(() => {
    gsap.fromTo(
      lineRef.current,
      { height: 0 },
      {
        height: '100%',
        ease: 'none',
        scrollTrigger: {
          trigger: sectionRef.current,
          start: 'top top',
          end: 'bottom bottom',
          scrub: true,
        },
      }
    )

    gsap.utils.toArray('.experience-card').forEach(card => {
      gsap.fromTo(
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
    })
  }, [])

  return (
    <section
      id='work-experience'
      ref={sectionRef}
      className='relative mx-auto max-w-3xl py-20'
    >
      <div
        ref={lineRef}
        className='absolute left-1/2 -translate-x-1/2 top-0 w-1 bg-cyan-500 rounded-full'
      />

      <h2 className='text-3xl font-extralight text-center mb-16 text-cyan-400'>
        Work Experience
      </h2>

      <div className='space-y-16 relative'>
        {experiences.slice().reverse().map((exp, i) => (
          <div key={i} className='relative mx-auto w-full max-w-md'>
            <WorkExperienceCard
              role={exp.role}
              company={exp.company}
              period={exp.period}
              points={exp.points}
              highlightPrompt={exp.highlightPrompt}
            />
          </div>
        ))}
      </div>
    </section>
  )
}

export default WorkExperience
