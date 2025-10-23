import React from 'react'

const ChatBubble = ({ sender, text }) => {
  const isUser = sender === 'user'

  return (
    <div
      className={`max-w-[80%] px-4 py-2 rounded-xl text-sm wrap-break-word
        ${isUser
          ? 'ml-auto bg-gradient-to-r from-cyan-500/30 to-purple-500/30 text-cyan-100'
          : 'mr-auto bg-gray-700/50 text-gray-200'}`}
    >
      {text}
    </div>
  )
}

export default ChatBubble
