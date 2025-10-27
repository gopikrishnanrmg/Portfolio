import React from 'react'

const SkillComponentMap = ({ name, src }) => {
  return (
    <div className="flex items-center px-2.5 py-1 gap-1.5
      bg-white/10 backdrop-blur-md rounded-lg
      text-white text-sm shadow-sm">
      <img className="w-4 h-4 object-contain" src={src} alt={name} />
      <span className="whitespace-nowrap">{name}</span>
    </div>
  )
}

export default SkillComponentMap
