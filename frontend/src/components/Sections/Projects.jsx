import React, { useEffect } from 'react'
import { gsap } from 'gsap'
import ProjectCard from './ProjectCard'

const Projects = () => {
  useEffect(() => {
    const animations = [];
    const scrollTriggers = [];

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
  }, [])

  const projects = [
    {
      id: 'ytlangf-1',
      title: 'YTLangF',
      description:
        'AI-powered, Peer-to-Peer Video Language Classifier. A sleek, decentralized AI application that cuts through YouTube clickbait by identifying the true spoken language behind video titles. Powered by a network of nodes connected via IPFS, it seamlessly classifies and shares language data in real-time. With bootstrap nodes on AWS for reliable discovery, you’ll always get accurate language insights—no more guessing games!',
      tech: ['FastAPI', 'Python', 'MongoDB', 'AWS', 'Bash', 'IPFS', 'Linux sockets', 'HTML', 'JavaScript'],
      banner: 'from-pink-500 via-purple-500 to-cyan-400',
      link: 'https://github.com/gopikrishnanrmg/YTLangF',
    },
    {
      id: 'migrator-1',
      title: 'MigratorEXT',
      description:
        'Developed in a team of 6, this tool supports partial and complete database migrations across heterogeneous systems. It enables seamless SQL ↔ NoSQL conversions, as well as structured/unstructured data transformations such as JSON ↔ XML and other format migrations. Built in Java, the tool leverages JDBC drivers and custom parsers to connect to multiple data sources, extract schemas, and transform records into the target format.',
      tech: ['Java', 'MongoDB', 'OracleDB', 'Maven', 'JDBC', 'XML', 'JSON'],
      banner: 'from-fuchsia-500 via-rose-400 to-amber-400',
      link: 'https://github.com/kshitij-halankar/MigratorExt',
    },
    {
      id: 'websearch-1',
      title: 'Web Search Engine',
      description:
        'Built a Java‑based web search engine in a team of 5, implementing inverted index with Trie, reverse index sorting, crawler, LRU caching, fuzzy search, and GUI for efficient query processing.',
      tech: ['Java', 'Swing', 'Maven', 'Trie', 'LRU Cache'],
      banner: 'from-teal-400 via-cyan-400 to-blue-500',
      link: 'https://github.com/kshitij-halankar/ACC-Web-Search-Engine',
    },
    {
      id: 'wampac-1',
      title: 'WAMPAC Blockchain Implementation',
      description:
        'Built consensus and file restore mechanisms for the Indian Navy’s WAMPAC blockchain project with Java, IPFS, and BFT, plus a Vue.js dashboard for real‑time system health and recovery monitoring.',
      tech: ['Java', 'Maven', 'IPFS', 'Socket programming', 'Byzantine Fault Tolerance', 'NLSS', 'Blockchain'],
      banner: 'from-red-500 via-pink-500 to-fuchsia-500',
      link: '',
    },
    {
      id: 'arp-1',
      title: 'ARP Spoof Protection System',
      description:
        'Developed a Dockerized security lab and application that protects nodes against ARP spoofing attacks using a PKI‑based challenge‑response procedure. The system employs RSA authentication, Scapy‑based ARP traffic monitoring, and honeypot deception to detect anomalies, repair ARP tables, and reverse spoofing attempts.',
      tech: ['Python', 'Docker', 'Bash', 'Scapy', 'RSA', 'Network Security'],
      banner: 'from-green-400 via-cyan-400 to-blue-500',
      link: 'https://github.com/gopikrishnanrmg/ARP-Spoof-Defence',
    },
    {
      id: 'ubuntu-1',
      title: 'Ubuntu Touch for Redmi Note 4X',
      description:
        'Led porting of Ubuntu Touch OS to Xiaomi Redmi Note 4X, patched and built Linux kernel, fixed hardware bugs (Wi‑Fi, GPS, camera, calls), and released builds with 12,000+ downloads on XDA.',
      tech: ['Linux Kernel', 'C', 'Bash', 'Make', 'Git', 'Ubuntu Touch', 'Android'],
      banner: 'from-lime-400 via-green-500 to-emerald-600',
      link: 'https://forum.xda-developers.com/t/rom-ubports-ubuntu-touch-for-redmi-note-4x.4069381/',
    },
    {
      id: 'nft-1',
      title: 'NFT Decentralized Application',
      description:
        'Designed and developed a decentralized NFT application inspired by CryptoKitties, enabling users to mint, mutate, and crossbreed NFTs with unique genetic traits. Each NFT carried a genome encoded in smart contracts, allowing deterministic inheritance and random mutations to create new, rare digital assets.',
      tech: ['React', 'Solidity', 'CSS', 'JavaScript', 'Truffle', 'web3js', 'Ethereum'],
      banner: 'from-purple-500 via-indigo-400 to-sky-400',
      link: '',
    },
    {
      id: 'ftp-1',
      title: 'FTP Application',
      description:
        'Developed a multi‑client FTP server and client in C with support for resuming interrupted file transfers, user authentication, directory management, and core FTP commands (STOR, RETR, LIST, CWD, RNFR/RNTO, etc.), enabling reliable and concurrent file operations over TCP.',
      tech: ['C', 'Linux Sockets'],
      banner: 'from-orange-400 via-pink-500 to-red-500',
      link: 'https://github.com/gopikrishnanrmg/ASP_FTP_app',
    },
    {
      id: 'shell-1',
      title: 'Custom Linux Shell',
      description:
        'Custom Linux shell in C supporting piped commands, built as part of an advanced systems programming course.',
      tech: ['C', 'Linux'],
      banner: 'from-indigo-500 via-sky-400 to-emerald-400',
      link: 'https://github.com/gopikrishnanrmg/ASP_Assignments',
      hidden: true
    },
    {
      id: 'goalhero-1',
      title: 'GoalHero',
      description:
        'Co‑developed GoalHero, a first‑person augmented reality mobile soccer game in a team of 8 using Unity, Blender, and C#. The game immerses players in a virtual penalty‑kick scenario, where they take shots on goal against a 3D animated goalkeeper projected into their real‑world environment through AR.',
      tech: ['Unity', 'Blender', 'C#'],
      banner: 'from-amber-500 via-lime-400 to-emerald-500',
      link: 'https://github.com/HarshaVardhanNaiduGangavarapu/goalhero',
    },
    {
      id: 'pinephone-1',
      title: 'PinePhone Tizen OS Port',
      description:
        'Ongoing project to port Tizen OS to PinePhone hardware. Built U-Boot, Linux kernel, and debugged hardware with UART.',
      tech: ['Linux Kernel', 'U-Boot', 'UART'],
      banner: 'from-rose-500 via-orange-400 to-yellow-400',
      link: '',
      hidden: true
    },
    {
      id: 'arduino-ac-1',
      title: 'Arduino Air Conditioner Remote',
      description:
        'Designed and built an Arduino‑based hardware system with an Android companion app to control SHARP air conditioners via Bluetooth. The project replicated the functionality of the original IR remote by capturing, decoding, and replaying infrared signals through custom circuits and mobile control.',
      tech: ['Java', 'Android', 'Gradle', 'Arduino', 'C++'],
      banner: 'from-emerald-500 via-teal-400 to-cyan-400',
      link: 'https://github.com/gopikrishnanrmg/Arduino_Remote_control_SHARP_AC',
    },
    {
      id: 'robot-1',
      title: 'Arduino Robot',
      description:
        'Obstacle-avoiding robot using Arduino and ultrasound sensors.',
      tech: ['Arduino', 'C++', 'Ultrasound Sensors'],
      banner: 'from-sky-400 via-cyan-400 to-indigo-500',
      link: '#',
      hidden: true
    },
    {
      id: 'radio-1',
      title: 'Mobile Radio App',
      description:
        'Co‑developed a LAN‑based Android radio application to broadcast the university’s radio station for students and staff. The app enabled seamless streaming of live audio over the local network, providing a lightweight, accessible platform for campus‑wide communication and entertainment.',
      tech: ['Java', 'Android', 'Gradle'],
      banner: 'from-yellow-400 via-amber-500 to-orange-600',
      link: 'https://github.com/Sparker0i/RadioAmrita',
    },
  ]

  return (
    <section id='projects' className='relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8'>
      <h2
        id='projects-title'
        className='text-3xl md:text-4xl lg:text-5xl font-extralight text-center mb-16 text-cyan-400'
      >
        Projects
      </h2>

      <div
        className="grid [grid-template-columns:repeat(auto-fit,minmax(22rem,1fr))] gap-10 mx-8"
      >
        {projects
          .filter(proj => !proj.hidden)
          .map((proj) => (
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
