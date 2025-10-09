import React from 'react'
import SplitText from './SplitText';
import LiquidEther from './LiquidEther';
const Hero = () => {
    const handleAnimationComplete = () => {
        console.log('All letters have animated!');
    };

    return (
        <section id='hero' className='flex flex-col md:flex-row justify-evenly pt-16 h-screen'>
            <div className='absolute inset-0'>
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
            </div>

            <div className='flex flex-1 flex-col flex-center text-2xl md:text-4xl space-y-2 text-center'>
                <SplitText
                    text='Hello 👋🏻,'
                    delay={100}
                    duration={0.6}
                    ease='power3.out'
                    splitType='chars'
                    from={{ opacity: 0, y: 40 }}
                    to={{ opacity: 1, y: 0 }}
                    threshold={0.1}
                    rootMargin='-100px'
                    textAlign='center'
                    className='text-white'
                    onLetterAnimationComplete={handleAnimationComplete}
                />

                <SplitText
                    text={'I\'m Gopikrishnan Rajeev'}
                    delay={70} 
                    duration={0.6}
                    ease='power3.out'
                    splitType='chars'
                    from={{ opacity: 0, y: 40 }}
                    to={{ opacity: 1, y: 0 }}
                    threshold={0.1}
                    rootMargin='-100px'
                    textAlign='center'
                    className='text-cyan-500'
                    onLetterAnimationComplete={handleAnimationComplete}
                />
            </div>
            <div className='flex flex-1 flex-center'>g</div>
        </section>
    )
}

export default Hero
