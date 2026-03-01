import React, { useState } from "react";

const Suggestions = ({ setInput }) => {
    const [view, setView] = useState(true)
  const [suggestion, setSuggestions] = useState([
    { id: 1, text: "What are Rajeev's core skills?" },
    {
      id: 2,
      text: "Send a message to Rajeev to ask if he would love to work on a project",
    },
  ]);

  const setText = (item) => {
    setInput(item.text)
    setView(false)
  } 

  const truncate = (str, maxLength) => {
    return str.length > maxLength ? str.substring(0, maxLength) + "..." : str;
  };

  return (
    view && (
      <div>
        <div className="flex flex-wrap gap-2 m-2 px-2 sm:px-4 sm:m-2">
          {suggestion.map((item) => (
            <div
              key={item.id}
              className="border rounded-xs p-1 sm:p-2 max-w-[80%] sm:max-w-[300px]
                         text-xs sm:text-sm cursor-pointer hover:text-cyan-500"
              onClick={() => setText(item)}
            >
              {truncate(item.text, 30)}
            </div>
          ))}
        </div>
      </div>
    )
  );  
};

export default Suggestions;
