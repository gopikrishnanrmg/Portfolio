import './App.css'
import { useState, useEffect } from 'react'
import ChatWidget from './components/Chat/ChatWidget'
import Header from './components/Header/Header'
import Hero from './components/Sections/Hero'
import Skills from './components/Sections/Skills'
import WorkExperience from './components/Sections/WorkExperience'
import Projects from './components/Sections/Projects'
import Testimonials from './components/Sections/Testimonials'
import LiquidEther from './components/Sections/LiquidEther'
import Footer from './components/Footer/Footer'
import CookieBanner from './components/Analytics/CookieBanner'
import { getOrCreateVisitorId, getOrCreateSessionId, sendAnalyticsEvent } from './components/Analytics/analyticsUtils'


function App() {

  const [showBanner, setShowBanner] = useState(false);

  useEffect(() => {
    const consent = localStorage.getItem("cookieConsent");

    if (consent === "accepted") {
      const visitorId = getOrCreateVisitorId();
      const sessionId = getOrCreateSessionId();
      sendAnalyticsEvent(
        {
          visitorId,
          sessionId,
          eventType: "page_view",
          page: window.location.pathname,
          eventData: {}
        });
    } else if (consent === "rejected") {
    } else {
      setShowBanner(true);
    }
  }, [])

  const handleAccept = () => {
    localStorage.setItem("cookieConsent", "accepted");
    const visitorId = getOrCreateVisitorId();
    const sessionId = getOrCreateSessionId();
    sendAnalyticsEvent(
      {
        visitorId,
        sessionId,
        eventType: "page_view",
        page: window.location.pathname,
        eventData: {}
      });
    setShowBanner(false);
  }

  const handleReject = () => {
    localStorage.setItem("cookieConsent", "rejected");
    setShowBanner(false);
  }

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
            autoIntensity={1.7}
            takeoverDuration={0.25}
            autoResumeDelay={3000}
            autoRampDuration={0.6}
          />
        </div>
        <Hero />
        <Skills />
        <WorkExperience />
        <Projects />
        <Testimonials />
        <ChatWidget />
      </main>
      {showBanner && <CookieBanner onAccept={handleAccept} onReject={handleReject} />}
      <Footer />
    </div>
  )
}

export default App
