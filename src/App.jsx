import './App.css'
import ChatMessage from './components/Chat/ChatMessage'
import Header from './components/Header/Header'
import Hero from './components/Sections/Hero'

function App() {

  return (
    <div className='min-h-screen flex flex-col'>
    <Header/>
    <main>
    <Hero></Hero>
    <ChatMessage/>
    </main>
    </div>
  )
}

export default App
