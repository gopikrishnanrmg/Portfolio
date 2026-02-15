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
        <div className="fixed right-4 bottom-20 w-[90vw] sm:w-80 md:w-96 lg:w-[420px] xl:w-[460px]
          h-[70vh] sm:h-[500px] md:h-[550px] lg:h-[600px] max-h-[80vh] z-50">
          <ChatWindow />
        </div>
      )}
    </>
  )
}

export default ChatWidget
