import React, { useState, useRef, useEffect } from 'react'
import { FiSend } from 'react-icons/fi'
import ChatBubble from './ChatBubble'

const ChatWindow = () => {
  const [messages, setMessages] = useState([
    { id: 1, sender: 'bot', text: 'Welcome! You can ask questions about Gopikrishnan & I will answer them for you.\n Example: "What are Gopikrishnan\'s skills?" or "Tell me about Gopikrishnan\'s projects."' },
    // { id: 2, sender: 'user', text: 'Hi there 👋' },
  ])
  const [input, setInput] = useState('')
  const messagesEndRef = useRef(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const sendMessage = () => {
    if (!input.trim()) return
    setMessages([...messages, { id: Date.now(), sender: 'user', text: input }])
    setInput('')
  }

  return (
    <div className="flex flex-col w-full max-w-sm md:max-w-md lg:max-w-lg h-full
                    rounded-2xl backdrop-blur-2xl bg-black/40 border border-gray-700
                    shadow-lg ring-1 ring-white/20 overflow-hidden z-50">

      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {messages.map(msg => (
          <ChatBubble key={msg.id} sender={msg.sender} text={msg.text} />
        ))}
        <div ref={messagesEndRef} />
      </div>

      <div className="p-3 border-t border-gray-700 bg-black/30 flex items-center gap-2">
        <textarea
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && !e.shiftKey && (e.preventDefault(), sendMessage())}
          placeholder="Type a message..."
          rows={2}
          className="flex-1 min-h-[40px] max-h-32 rounded-lg px-2 py-2 bg-black/50 text-gray-200
               border border-gray-600 focus:outline-none focus:border-cyan-400 resize-none"
        />
        <button
          onClick={sendMessage}
          className="w-10 h-10 sm:w-12 sm:h-12 lg:w-14 lg:h-14
                    flex items-center justify-center rounded-full
                    bg-gradient-to-r from-cyan-500 to-purple-500
                    text-white hover:opacity-90 transition shrink-0"
        >
          <FiSend className="w-4 h-4 sm:w-5 sm:h-5 lg:w-6 lg:h-6" />
        </button>
      </div>
    </div>
  )
}

export default ChatWindow
