import React from 'react'
import SplitText from './SplitText';
// import LiquidEther from './LiquidEther';
import HeroThree from './HeroThree';
import { FaLinkedinIn } from 'react-icons/fa';
import { FaGithub } from 'react-icons/fa';
import Stats from './Stats';

const Hero = () => {
    const handleAnimationComplete = () => {
        console.log('All letters have animated!');
    };

    return (
        <section id='hero' className='flex flex-col md:flex-row justify-evenly text-2xl md:text-4xl pt-16 min-h-screen z-10'>
            {/* <div className='absolute inset-0'>
                <LiquidEther
                    colors={['#5227FF', '#FF9FFC', '#B19EEF']}
                    mouseForce={20}
                    cursorSize={100}
                    isViscous={false}
                    viscous={30}
                    iterationsViscous={32}
                    iterationsPoisson={32}
                    resolution={0.5}
                    isBounce={false}
                    autoDemo={true}
                    autoSpeed={0.5}
                    autoIntensity={0.7}
                    takeoverDuration={0.25}
                    autoResumeDelay={3000}
                    autoRampDuration={0.6}
                />
            </div> */}
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
                    onLetterAnimationComplete={handleAnimationComplete}
                />

                <SplitText
                    text='Gopikrishnan Rajeev'
                    className='text-cyan-500 text-2xl md:text-4xl'
                    delay={70} 
                    duration={0.6}
                    ease='power3.out'
                    splitType='chars'
                    from={{ opacity: 0, y: 40 }}
                    to={{ opacity: 1, y: 0 }}
                    threshold={0.1}
                    rootMargin='-100px'
                    textAlign='center'
                    onLetterAnimationComplete={handleAnimationComplete}
                />
                <p className='text-base md:text-xl'>Software Engineer with over 
                    5+ years of experience in all parts of SDLC including Design, Development, 
                    Testing, and Deployment with specialization in the <span className='text-cyan-500'>Java </span> 
                    ecosystem.</p>
                <div className='flex flex-center gap-10 mt-5'>
                    <a className='hover:text-cyan-500' href='https://linkedin.com/in/gopikrishnan-rajeev-2618a913b/'><FaLinkedinIn/></a>
                    <a className='hover:text-cyan-500' href='https://github.com/gopikrishnanrmg'><FaGithub/></a>
                </div>
                <div className='flex flex-col md:flex-row md:flex-center gap-10 md:gap-5 flex-wrap mt-10 md:mt-20'>
                    <Stats title='30+' description='Technologies Across Projects'/>
                    <div className='hidden md:block w-px h-8 bg-gray-500/50'></div>
                    <Stats title='25+' description='Projects completed'/>
                    <div className='hidden md:block w-px h-8 bg-gray-500/50'></div>
                    <Stats title='12000+' description='Users'/>
                </div>
            </div>
            <div className='flex flex-1 flex-center'>
                <HeroThree/>
            </div>
        </section>
    )
}

export default Hero
