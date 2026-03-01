import React, { useState, useRef, useEffect } from "react";
import { FiSend } from "react-icons/fi";
import ChatBubble from "./ChatBubble";
import Suggestions from "./Suggestions";

const ChatWindow = ({ messages, setMessages, onClose }) => {

  const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL;

  const [input, setInput] = useState("");
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = async () => {
    if (!input.trim()) return;
    setMessages((messages) => [
      ...messages,
      { id: Date.now(), sender: "user", text: input },
    ]);
    setInput("");

    try {
      const res = await fetch(`${API_BASE_URL}/ai/api/v1/chat`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ content: input }),
      });

      const data = await res.json();

      setMessages((messages) => [
        ...messages,
        { id: Date.now(), sender: "bot", text: data.reply },
      ]);
    } catch (err) {
      console.error("Error sending message:", err);
    }
  };

  return (
    <div
      className="relative flex flex-col w-full h-full rounded-none md:rounded-2xl 
        backdrop-blur-2xl bg-black/40 border border-gray-700 shadow-lg 
        ring-1 ring-white/20 overflow-hidden"
    >
      <div className="flex items-center justify-end p-3 border-b border-gray-700 bg-black/30">
        <button
          onClick={onClose}
          className="text-white w-8 h-8 flex items-center justify-center rounded-full 
            bg-black/40 border border-gray-600 hover:bg-cyan-600"
        >
          ✕
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {messages.map((msg) => (
          <ChatBubble key={msg.id} sender={msg.sender} text={msg.text} />
        ))}
        <div ref={messagesEndRef} />
      </div>
      <Suggestions setInput={setInput} />

      <div className="p-3 border-t border-gray-700 bg-black/30 flex items-center gap-2">
        <textarea
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) =>
            e.key === "Enter" &&
            !e.shiftKey &&
            (e.preventDefault(), sendMessage())
          }
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
  );
};

export default ChatWindow;
