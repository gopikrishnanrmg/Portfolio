import React, { useState } from "react";
import { AiFillMessage } from "react-icons/ai";
import ChatWindow from "./ChatWindow";

const ChatWidget = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([]);

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
        <div
          className="
            fixed inset-0 z-50
            flex items-end justify-center
            md:inset-y-4 md:right-4 md:left-auto
            md:justify-end
            pointer-events-none
          "
        >
          <div
            className="
              pointer-events-auto
              w-full h-full
              md:w-[420px]
              md:h-full
            "
          >
            <ChatWindow
              messages={messages}
              setMessages={setMessages}
              onClose={() => setIsOpen(false)}
            />
          </div>
        </div>
)}

    </>
  );
};

export default ChatWidget;
