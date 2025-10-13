import React from 'react'

const SkillComponentMap = (props) => {
  return (
<div className='h-full flex items-center justify-center text-lg p-2 gap-2 
  bg-white/10 backdrop-blur-2xl rounded-2xl'>
  <img className='w-6 h-6' src={props.src} alt={props.name}/>
  <div className='text-sm leading-tight'>{props.name}</div>
</div>
  )
}

export default SkillComponentMap
