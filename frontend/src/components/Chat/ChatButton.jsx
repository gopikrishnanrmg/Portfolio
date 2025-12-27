import React, { useState } from 'react'
import { AiFillMessage } from 'react-icons/ai'
import ChatWindow from './ChatWindow'

const ChatWidget = () => {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="fixed right-5 bottom-5 w-12 h-12 z-50 flex items-center justify-center
                   rounded-full bg-black/60 border border-gray-700 shadow-lg
                   hover:border-cyan-500 hover:shadow-cyan-500/30 transition"
      >
      <AiFillMessage className="w-6 h-6 sm:w-7 sm:h-7 lg:w-8 lg:h-8 text-cyan-400" />
      </button>

      {isOpen && (
        <div className="fixed right-10 bottom-20 w-80 h-[500px] z-50">
          <ChatWindow />
        </div>
      )}
    </>
  )
}

export default ChatWidget
