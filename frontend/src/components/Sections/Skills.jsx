import React, { useEffect } from 'react'
import { gsap } from 'gsap'
import SkillsCard from './SkillsCard'

const Skills = () => {
  useEffect(() => {
    const animations = [];
    const scrollTriggers = [];

    gsap.utils.toArray('.skills-card').forEach(card => {
      const anim = gsap.fromTo(
        card,
        {
          opacity: 0,
          z: 300,
          scale: 1.05,
          y: -50,
          transformPerspective: 1000,
        },
        {
          opacity: 1,
          z: 0,
          scale: 1,
          y: 0,
          ease: 'power3.out',
          scrollTrigger: {
            trigger: card,
            start: 'top 85%',
            end: 'top 40%',
            scrub: true,
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
  }, [])

    const architecture = [
        { name: 'UML', src: '/icons/UML.svg' },
        { name: 'Design Patterns', src: '/icons/Design-Patterns.svg' },
        { name: 'Microservices', src: '/icons/Microservices.svg' },
        { name: 'Blockchain', src: '/icons/Blockchain.svg' },
    ]

    const development = [
        { name: 'Java', src: '/icons/Java.svg' },
        { name: 'Spring', src: '/icons/Spring.svg' },
        { name: 'REST APIs', src: '/icons/REST.svg' },
        { name: 'Hibernate', src: '/icons/Hibernate.svg' },
        { name: 'IntelliJ', src: '/icons/IntelliJ.svg' },
        { name: 'SQL Server', src: '/icons/SQL-Server.svg' },
        { name: 'InfluxDB', src: '/icons/InfluxDB.svg' },
        { name: 'MongoDB', src: '/icons/MongoDB.svg' },
        { name: 'Python', src: '/icons/Python.svg' },
        { name: 'JavaScript', src: '/icons/JavaScript.svg' },
        { name: 'React', src: '/icons/React.svg' },
        { name: 'HTML', src: '/icons/HTML5.svg' },
        { name: 'CSS', src: '/icons/CSS3.svg' },
        { name: 'Grafana', src: '/icons/Grafana.svg' },
        { name: 'Bash', src: '/icons/Bash.svg' },
        { name: 'PowerShell', src: '/icons/Powershell.svg' },
        { name: 'Git', src: '/icons/Git.svg' }
    ]

    const testing = [
        { name: 'JUnit', src: '/icons/JUnit.svg' },
        { name: 'JMeter', src: '/icons/JMeter.svg' },
        { name: 'Performance Testing', src: '/icons/Performance.svg' },
        { name: 'Selenium', src: '/icons/Selenium.svg' }
    ]

    const devops = [
        { name: 'Maven', src: '/icons/Maven.svg' },
        { name: 'Docker', src: '/icons/Docker.svg' },
        { name: 'Podman', src: '/icons/Podman.svg' },
        { name: 'Github Actions', src: '/icons/GitHub-Actions.svg' },
        { name: 'Linux', src: '/icons/Linux.svg' },
        { name: 'CI/CD', src: '/icons/Pipeline.svg' },
        { name: 'AWS', src: '/icons/AWS.svg' },
    ]

    const miscellaneous = [
        { name: 'C#', src: '/icons/Csharp.svg' },
        { name: 'C', src: '/icons/C.svg' },
        { name: 'C++', src: '/icons/C++.svg' },
        { name: 'R', src: '/icons/R.svg' },
        { name: 'Solidity', src: '/icons/Solidity.svg' },
        { name: 'Android App', src: '/icons/Android.svg' },
        { name: 'Azure', src: '/icons/Azure.svg' },
        { name: 'Minikube', src: '/icons/Minikube.svg' },
        { name: 'Arduino', src: '/icons/Arduino.svg' },
        { name: 'Tailwind', src: '/icons/Tailwind.svg' },
        { name: 'ThreeJS', src: '/icons/Threejs.svg' },
        { name: 'Bootstrap', src: '/icons/Bootstrap.svg' },
        // { name: 'VSCode', src: '/icons/vscode.svg' },
        { name: 'Unity', src: '/icons/Unity.svg' },
        { name: 'OpenAi APIs', src: '/icons/OpenAi.svg' },
        { name: 'Nodejs', src: '/icons/Node.svg' },
        { name: 'Express', src: '/icons/Express.svg' },
        // { name: 'Jupyter', src: '/icons/Jupyter.svg' },
        { name: 'GitLab', src: '/icons/GitLab.svg' },
        { name: 'FastAPI', src: '/icons/FastAPI.svg' },
        { name: 'Flask', src: '/icons/Flask.svg' },
        { name: 'Jira', src: '/icons/Jira.svg' },
        { name: 'Nginx', src: '/icons/Nginx.svg' },
        { name: 'OpenCV', src: '/icons/OpenCV.svg' },
        { name: 'Blender', src: '/icons/Blender.svg' }
    ]    

  return (
    <section id='skills' className='relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8 overflow-x-hidden'>
      <h2
        id='skills-title'
        className='text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400'
      >
        Skills
      </h2>

      <div
        className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8'
      >
        <SkillsCard title='Architecture' techMap={architecture} />
        <SkillsCard title='Development' techMap={development} />
        <SkillsCard title='Testing' techMap={testing} />
        <SkillsCard title='Devops' techMap={devops} />
        <SkillsCard
          title='Additional Exposure (From projects and learning)'
          techMap={miscellaneous}
        />
      </div>
    </section>
  )
}

export default Skills