import React, { useState } from "react";
import { AiFillMessage } from "react-icons/ai";
import ChatWindow from "./ChatWindow";

const ChatWidget = () => {
  const [isOpen, setIsOpen] = useState(false);

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
          class="fixed inset-0 z-50 w-screen h-screen
          md:w-[420px] md:h-[600px] md:right-4 md:bottom-20
          md:left-auto md:top-auto md:rounded-2xl"
        >
          <ChatWindow onClose={() => setIsOpen(false)}/>
        </div>
      )}
    </>
  );
};

export default ChatWidget;
