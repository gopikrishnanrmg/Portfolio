import React, { useRef, useEffect } from 'react'
import SkillComponentMap from './SkillComponentMap'
import { gsap } from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'
gsap.registerPlugin(ScrollTrigger)

const SkillComponent = (props) => {
    const containerRef = useRef(null)
    const itemRefs = useRef([])

    useEffect(() => {
        if (!containerRef.current || !itemRefs.current) return

        const directions = props.techMap.map(() => {
            const angle = Math.random() * 2 * Math.PI
            const distance = 120 + Math.random() * 80 // px
            return {
                x: Math.cos(angle) * distance,
                y: Math.sin(angle) * distance
            }
        })

        itemRefs.current.forEach((el, i) => {
            gsap.fromTo(
                el,
                { x: directions[i].x, y: directions[i].y, opacity: 0 },
                {
                    x: 0,
                    y: 0,
                    opacity: 1,
                    duration: 1,
                    ease: 'power3.out',
                    scrollTrigger: {
                        trigger: containerRef.current,
                        start: 'top 80%',
                        end: 'bottom 20%',
                        toggleActions: 'play reverse play reverse',
                        onLeave: () => {
                            gsap.to(el, {
                                x: directions[i].x,
                                y: directions[i].y,
                                opacity: 0.5,
                                duration: 0.8,
                                ease: 'power2.in'
                            })
                        },
                        onEnterBack: () => {
                            gsap.to(el, {
                                x: 0,
                                y: 0,
                                opacity: 1,
                                duration: 1,
                                ease: 'power3.out'
                            })
                        }
                    }
                }
            )
        })
    }, [props.techMap])

    return (
        <div className='flex flex-col' ref={containerRef}>
            <div className='mb-5'>{props.title}</div>
            <div className='flex flex-row flex-wrap gap-5'>
                {props.techMap.map((item, i) => (
                    <div
                        key={item.name}
                        ref={el => itemRefs.current[i] = el}
                        style={{ willChange: 'transform, opacity' }}
                    >
                        <SkillComponentMap name={item.name} src={item.src} />
                    </div>
                ))}
            </div>
        </div>
    )
}

export default SkillComponent
