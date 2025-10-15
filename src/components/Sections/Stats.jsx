import React, { useEffect, useRef } from 'react'
import gsap from 'gsap'

const Stats = ({ title, description }) => {
  const numberRef = useRef(null)

  useEffect(() => {
    const obj = { val: 1 }

    gsap.fromTo(
      obj,
      { val: 1 },
      {
        val: parseInt(title, 10),
        duration: 2,
        ease: 'power1.out',
        onUpdate: () => {
          if (numberRef.current) {
            numberRef.current.textContent = Math.floor(obj.val).toLocaleString()
          }
        }
      }
    )
  }, [title])

  return (
    <div className='flex flex-col items-center'>
      <div
        ref={numberRef}
        className='text-2xl md:text-3xl font-bold tabular-nums w-24 text-center'
      >
        {title}
      </div>
      <div className='text-xs md:text-sm text-gray-400'>
        {description}
      </div>
    </div>
  )
}

export default Stats
