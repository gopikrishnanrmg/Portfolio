import React from 'react'
import SkillComponentMap from './SkillComponentMap'

const SkillComponent = (props) => {
    return (
        <div className='flex flex-col'>
            <div className='mb-5'>{props.title}</div>
            {/* <div className='grid grid-cols-2 sm:grid-cols-2 lg:grid-cols-3 gap-x-2 gap-y-6 md:p-6 text-center'>
                {props.techMap.map(item => (
                    <SkillComponentMap key={item.name} name={item.name} src={item.src} />
                ))}
            </div> */}
            <div className='flex flex-row flex-wrap gap-5'>
                {props.techMap.map(item => (
                    <SkillComponentMap key={item.name} name={item.name} src={item.src} />
                ))}
            </div>
        </div>
    )
}

export default SkillComponent
