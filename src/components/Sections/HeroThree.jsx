import React, { useRef } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { OrbitControls, useGLTF } from '@react-three/drei'

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
  return (
    <Canvas camera={{ position: [0, 200, 800], fov: 50 }}>
      <ambientLight intensity={3} />
      <directionalLight position={[5, 5, 5]} />
      <Model scale={1.5} />
      <OrbitControls />
    </Canvas>
  )
}
