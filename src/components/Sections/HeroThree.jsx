import React, { useRef, useState, useEffect } from 'react'
import { Canvas, useFrame, useThree } from '@react-three/fiber'
import { OrbitControls, useGLTF } from '@react-three/drei'

function useIsMobile() {
  const [isMobile, setIsMobile] = useState(false)

  useEffect(() => {
    const check = () => setIsMobile(window.innerWidth < 768)
    check()
    window.addEventListener('resize', check)
    return () => window.removeEventListener('resize', check)
  }, [])

  return isMobile
}

function Model(props) {
  const { scene } = useGLTF('/models/lowpoly_island.glb')
  const ref = useRef()

  useFrame((state, delta) => {
    if (ref.current) {
      ref.current.rotation.y += delta * 0.5
    }
  })

  return <primitive ref={ref} object={scene} {...props} />
}

export default function HeroThree() {
  const isMobile = useIsMobile()

  return (
    <Canvas camera={{ position: [0, 200, 800], fov: 50 }}>
      <ambientLight intensity={3} />
      <directionalLight position={[5, 5, 5]} />
      <Model scale={isMobile ? 2.2 : 1.5} />
      <OrbitControls enableZoom={false} />
    </Canvas>
  )
}
