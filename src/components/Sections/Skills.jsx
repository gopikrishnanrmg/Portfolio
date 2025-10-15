import React from 'react'
import SkillComponent from './SkillComponent'

const Skills = () => {

    const architecture = [
        { name: 'UML', src: '/icons/UML.svg' },
        { name: 'Design Patterns', src: '/icons/Design-Patterns.svg' },
        { name: 'Microservices', src: '/icons/Microservices.svg' },
        { name: 'Blockchain', src: '/icons/Blockchain.svg' },
    ]

    const development = [
        { name: 'Java', src: '/icons/Java.svg' },
        { name: 'Spring', src: '/icons/Spring.svg' },
        { name: 'REST APIs', src: '/icons/REST.svg' },
        { name: 'Hibernate', src: '/icons/Hibernate.svg' },
        { name: 'IntelliJ', src: '/icons/IntelliJ.svg' },
        { name: 'SQL Server', src: '/icons/SQL-Server.svg' },
        { name: 'InfluxDB', src: '/icons/InfluxDB.svg' },
        { name: 'MongoDB', src: '/icons/MongoDB.svg' },
        { name: 'Python', src: '/icons/Python.svg' },
        { name: 'JavaScript', src: '/icons/JavaScript.svg' },
        { name: 'React', src: '/icons/React.svg' },
        { name: 'HTML', src: '/icons/HTML5.svg' },
        { name: 'CSS', src: '/icons/CSS3.svg' },
        { name: 'Grafana', src: '/icons/Grafana.svg' },
        { name: 'Bash', src: '/icons/Bash.svg' },
        { name: 'PowerShell', src: '/icons/Powershell.svg' },
        { name: 'Git', src: '/icons/Git.svg' }
    ]

    const testing = [
        { name: 'JUnit', src: '/icons/JUnit.svg' },
        { name: 'JMeter', src: '/icons/JMeter.svg' },
        { name: 'Performance Testing', src: '/icons/Performance.svg' },
        { name: 'Selenium', src: '/icons/Selenium.svg' }
    ]

    const devops = [
        { name: 'Maven', src: '/icons/Maven.svg' },
        { name: 'Docker', src: '/icons/Docker.svg' },
        { name: 'Podman', src: '/icons/Podman.svg' },
        { name: 'Github Actions', src: '/icons/GitHub-Actions.svg' },
        { name: 'Linux', src: '/icons/Linux.svg' },
        { name: 'CI/CD', src: '/icons/Pipeline.svg' },
        { name: 'AWS', src: '/icons/AWS.svg' },
    ]

    const miscellaneous = [
        { name: 'C#', src: '/icons/Csharp.svg' },
        { name: 'C', src: '/icons/C.svg' },
        { name: 'C++', src: '/icons/C++.svg' },
        { name: 'R', src: '/icons/R.svg' },
        { name: 'Solidity', src: '/icons/Solidity.svg' },
        { name: 'Android App', src: '/icons/Android.svg' },
        { name: 'Azure', src: '/icons/Azure.svg' },
        { name: 'Minikube', src: '/icons/Minikube.svg' },
        { name: 'Arduino', src: '/icons/Arduino.svg' },
        { name: 'Tailwind', src: '/icons/Tailwind.svg' },
        { name: 'ThreeJS', src: '/icons/Threejs.svg' },
        { name: 'Bootstrap', src: '/icons/Bootstrap.svg' },
        // { name: 'VSCode', src: '/icons/vscode.svg' },
        { name: 'Unity', src: '/icons/Unity.svg' },
        { name: 'OpenAi APIs', src: '/icons/OpenAi.svg' },
        { name: 'Nodejs', src: '/icons/Node.svg' },
        { name: 'Express', src: '/icons/Express.svg' },
        // { name: 'Jupyter', src: '/icons/Jupyter.svg' },
        { name: 'GitLab', src: '/icons/GitLab.svg' },
        { name: 'FastAPI', src: '/icons/FastAPI.svg' },
        { name: 'Flask', src: '/icons/Flask.svg' },
        { name: 'Jira', src: '/icons/Jira.svg' },
        { name: 'Nginx', src: '/icons/Nginx.svg' },
        { name: 'OpenCV', src: '/icons/OpenCV.svg' },
        { name: 'Blender', src: '/icons/Blender.svg' }
    ]    

    return (
        // <section id='skills' className='flex justify-around min-h-screen pt-20 z-10'>
        //      <div className='flex flex-col justify-evenly w-full text-2xl md:text-4xl mx-1'>
        //         <SkillComponent title='Architecture' techMap={architecture}/>
        //         {/* <div className='hidden md:block h-px min-w-screen bg-gray-500/50'></div> */}
        //         <SkillComponent title='Development' techMap={development}/>
        //         <div>Testing</div>
        //         <div>Deployment</div>
        //     </div>
        // </section>
        <section id='skills' className='flex flex-col text-xl md:text-2xl gap-15 pt-20 mx-5'>
            <div className='flex-1'>
                <SkillComponent title='Architecture' techMap={architecture} />
            </div>
            <div className='flex-1'>
                <SkillComponent title='Development' techMap={development} />
            </div>
            <div className='flex-1'>
                <SkillComponent title='Testing' techMap={testing} />
            </div>
            <div className='flex-1'>
                <SkillComponent title='Devops' techMap={devops} />
            </div>
            <div className='flex-1'>
                <SkillComponent title='Additional tools I have used in the past' techMap={miscellaneous} />
            </div>
        </section>
    )
}

export default Skills
