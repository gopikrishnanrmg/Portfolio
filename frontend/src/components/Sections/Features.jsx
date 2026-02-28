import React, { useEffect }  from 'react'
import { gsap } from 'gsap'
import SkillComponentMap from './SkillComponentMap'

const Features = () => {

    useEffect(() => {
        const anim = gsap.to('.featurecard-title', {
          backgroundPosition: '200% center',
          duration: 8,
          repeat: 0,
          ease: 'linear',
        })
      
        return () => anim.kill()
      }, [])
      
  return (
    <section
    id='features'
    className='relative mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] py-20 px-8'
    >
        <h2
        id='features-title'
        className='text-2xl md:text-3xl lg:text-4xl font-extralight text-center mb-16 text-cyan-400'
        >
        Features
        </h2>

        <div className='flex flex-col lg:flex-row gap-6'>
            <div className='flex-[2] flex flex-col gap-4 rounded-2xl p-6 w-full min-h-40 backdrop-blur-2xl 
            bg-black/40 border border-gray-700 shadow-lg ring-1 ring-white/20 overflow-hidden
            bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]
            hover:border-cyan-500 hover:shadow-cyan-500/30 transition-all duration-300'>

                <h3
                className='featurecard-title inline-block text-xl font-semibold mb-1 z-10
                            bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                            bg-clip-text text-transparent
                            [background-size:300%_300%] [background-position:0%_center]'
                >
                Feature Summary
                </h3>

                <p className='text-sm md:text-base leading-relaxed text-gray-300'>
                    This is a microservice app hosted on AWS (Backend) and Dfinity IC blockchain (Frontend).
                    If you want to geek out and explore the full architecture, click{' '}
                    <a
                    href='https://github.com/gopikrishnanrmg/Portfolio'
                    target='_blank'
                    rel='noopener noreferrer'
                    className='text-cyan-400 underline underline-offset-4 hover:text-cyan-300 
                                transition-all duration-200 hover:drop-shadow-[0_0_6px_#22d3ee]'
                    >
                    here
                    </a>.
                </p>

                <div className='grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4'>
                    {[
                        {
                        text: 'Microservice Architecture',
                        gradient: 'bg-gradient-to-r from-orange-700 via-amber-600 to-yellow-600'
                        },
                        {
                        text: 'AI powered chatbot with messaging (MCP)',
                        gradient: 'bg-gradient-to-r from-purple-700 via-indigo-600 to-blue-600'
                        },
                        {
                        text: 'Visitor Analytics Dashboard',
                        gradient: 'bg-gradient-to-r from-emerald-600 via-teal-600 to-cyan-600'
                        },
                        {
                        text: 'Service health checks and alerting system',
                        gradient: 'bg-gradient-to-r from-rose-700 via-purple-700 to-indigo-700'
                        },
                        {
                        text: 'Blockchain integration',
                        gradient: 'bg-gradient-to-r from-blue-700 via-sky-600 to-cyan-600'
                        },
                        {
                        text: 'Automated CI/CD & docker support',
                        gradient: 'bg-gradient-to-r from-red-700 via-rose-700 to-pink-700'
                        }
                    ].map((item, idx) => (
                        <div
                        key={idx}
                        className={`
                            ${item.gradient}
                            px-4 py-6 rounded-none text-white font-semibold
                            border border-transparent
                            transition-all duration-300 cursor-pointer
                            hover:border-white hover:shadow-[0_0_12px_rgba(255,255,255,0.5)]
                        `}
                        >
                        {item.text}
                        </div>
                    ))}
                </div>

            </div>

            <div className='flex-[1] rounded-2xl p-4 w-full min-h-40 backdrop-blur-2xl 
                bg-black/40 border border-gray-700 shadow-lg ring-1 ring-white/20 overflow-hidden
                bg-[url(/backgrounds/honeycomb.svg)] bg-[length:120px_69.28px]
                hover:border-cyan-500 hover:shadow-cyan-500/30'>
                    <h3
                    className='featurecard-title inline-block text-xl font-semibold mb-3 z-10
                                bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                                bg-clip-text text-transparent
                                [background-size:300%_300%] [background-position:0%_center]'
                    >
                    Tech Stack
                    </h3>
                    <div className='flex flex-wrap gap-2 z-10'>
                        {[{'name':'Java','url':'/icons/Java.svg'},
                        {'name':'Spring','url':'/icons/Spring.svg'},
                        {'name':'Hibernate','url':'/icons/Hibernate.svg'},
                        {'name':'Microservices','url':'/icons/Microservices.svg'},
                        {'name':'ICP','url':'/icons/Icp.svg'},
                        {'name':'AWS','url':'/icons/AWS.svg'},
                        {'name':'Postgres','url':'/icons/Postgresql.svg'},
                        {'name':'Python','url':'/icons/Python.svg'},
                        {'name':'FastApi','url':'/icons/FastAPI.svg'},
                        {'name':'LangChain','url':'/icons/Langchain.svg'},
                        {'name':'MCP','url':'/icons/MCP.svg'}, 
                        {'name':'Redis','url':'/icons/Redis.svg'},
                        {'name':'RabbitMQ','url':'/icons/Rabbitmq.svg'},
                        {'name':'Grafana','url':'/icons/Grafana.svg'},
                        {'name':'Prometheus','url':'/icons/Prometheus.svg'},
                        {'name':'MongoDB','url':'/icons/MongoDB.svg'},
                        {'name':'Maven','url':'/icons/Maven.svg'},
                        {'name':'Docker','url':'/icons/Docker.svg'},
                        {'name':'Github Actions','url':'/icons/GitHub-Actions.svg'},
                        {'name':'JUnit','url':'/icons/JUnit.svg'},
                        {'name':'React','url':'/icons/React.svg'},
                        {'name':'Tailwind','url':'/icons/Tailwind.svg'},
                        {'name':'JavaScript','url':'/icons/JavaScript.svg'},
                        {'name':'Telegram','url':'/icons/Telegram.svg'},
                        {'name':'GitHub','url':'/icons/GitHub.svg'},
                    ]
                        .map(item => (
                        <SkillComponentMap
                            key={item.name}
                            name={item.name}
                            src={`${item.url}`}
                        />
                        ))}
                    </div>
            </div>
        </div>

  </section>
  )
}

export default Features