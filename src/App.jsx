import './App.css'
import ChatMessage from './components/Chat/ChatMessage'
import Header from './components/Header/Header'
import Hero from './components/Sections/Hero'
import Skills from './components/Sections/Skills'
import WorkExperience from './components/Sections/WorkExperience'
import LiquidEther from './components/Sections/LiquidEther'
import Footer from './components/Footer/Footer'

function App() {
  return (
    <div className="relative min-h-screen flex flex-col">
      <Header />
      <main className="relative flex-1">
        <div className="fixed inset-0 z-0 pointer-events-none">
          <LiquidEther
            className="w-full h-full"
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
        <Hero />
        <Skills />
        <WorkExperience />
        <ChatMessage />
      </main>
      <Footer/>
    </div>
  )
}

export default App
