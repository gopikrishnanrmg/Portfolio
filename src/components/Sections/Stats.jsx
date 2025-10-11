import React from 'react'

const Stats = (props) => {
  return (
    <div className='flex'>
      <div className='text-2xl md:text-3xl'>{props.title}</div>
      <div className='text-xs md:text-xs'>{props.description}</div>
    </div>
  )
}

export default Stats