import React from 'react'
import SplitText from './SplitText';
import HeroThree from './HeroThree';
import { FaLinkedinIn,FaGithub } from 'react-icons/fa';
import EmailWithQR from './EmailWithQR';
import Stats from './Stats';

const Hero = () => {

    return (
        <section id='hero' className='flex flex-col md:flex-row justify-between w-full
        pt-28 md:pt-16 min-h-[90vh] md:min-h-screen px-6 md:px-8 z-10'>
            <div className='flex flex-1 flex-col flex-center space-y-2 mx-2 text-center'>
                <SplitText
                    text={'Hello 👋🏻, I\'m'}
                    className='text-xl md:text-2xl'
                    delay={100}
                    duration={0.6}
                    ease='power3.out'
                    splitType='chars'
                    from={{ opacity: 0, y: 40 }}
                    to={{ opacity: 1, y: 0 }}
                    threshold={0.1}
                    rootMargin='-100px'
                    textAlign='center'
                />

                <SplitText
                    text='Gopikrishnan Rajeev'
                    className='text-cyan-500 text-2xl md:text-4xl lg:text-5xl'
                    delay={70} 
                    duration={0.6}
                    ease='power3.out'
                    splitType='chars'
                    from={{ opacity: 0, y: 40 }}
                    to={{ opacity: 1, y: 0 }}
                    threshold={0.1}
                    rootMargin='-100px'
                    textAlign='center'
                />
                <p className='text-base md:text-xl'>Welcome to my <span className='text-cyan-500'>AI-powered Microservices-based </span> 
                    portfolio hosted on AWS and ICP blockchain!<br></br>Click <a href='https://github.com/gopikrishnanrmg/Portfolio'>
                    <span className='hover:text-cyan-300 underline underline-offset-4 transition-all duration-200 
                    hover:drop-shadow-[0_0_6px_#22d3ee]'>here</span></a> to Learn more<br></br><br></br>I'm a Software Engineer with over 
                    5+ years of experience in all parts of SDLC including Design, Development, 
                    Testing, and Deployment with specialization in the <span className='text-cyan-500'>Spring </span> 
                    ecosystem.</p>
                    <div className='flex flex-center gap-10 mt-5 text-2xl md:text-4xl'>
                        <EmailWithQR/>
                        <a className='hover:text-cyan-500' href='https://linkedin.com/in/gopikrishnan-rajeev-2618a913b/'><FaLinkedinIn/></a>
                        <a className='hover:text-cyan-500' href='https://github.com/gopikrishnanrmg'><FaGithub/></a>
                    </div>
                    <div className='flex flex-col md:flex-row md:flex-center gap-10 md:gap-5 items-center justify-center flex-wrap mt-10 md:mt-20'>
                        <Stats title='30' description='Technologies Across Projects'/>
                        <div className='hidden md:block w-px h-8 bg-gray-500/50'></div>
                        <Stats title='25' description='Projects completed'/>
                        <div className='hidden md:block w-px h-8 bg-gray-500/50'></div>
                        <Stats title='12000' description='Project users'/>
                    </div>
            </div>
            <div className='flex flex-1 flex-center'>
                <HeroThree/>
            </div>
        </section>
    )
}

export default Hero
