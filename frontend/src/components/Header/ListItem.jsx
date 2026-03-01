import React from "react";
import gsap from "gsap";
import { ScrollToPlugin } from "gsap/ScrollToPlugin";

gsap.registerPlugin(ScrollToPlugin);

const ListItem = ({ id, title, icon, active, type, onClick }) => {
  const scrollToSection = (id) => {
    gsap.to(window, { duration: 1, scrollTo: id, ease: "power2.inOut" });
  };

  const isActive = active === id;

  if (type === "external") {
    return (
      <li
        className={`px-2 cursor-pointer rounded-2xl transition-colors font-light
          text-white hover:text-cyan-500`}
        onClick={onClick}
      >
        <a
          href={id}
          target="_blank"
          rel="noopener noreferrer"
          className="flex items-center gap-2"
          onClick={onClick}
        >
          {icon} {title}
        </a>
      </li>
    );
  }

  return (
    <li
      className={`px-2 cursor-pointer rounded-2xl transition-colors font-light
    ${isActive ? "text-cyan-400" : "text-white"} 
    hover:text-cyan-500`}
    onClick={() => { scrollToSection(id); onClick?.(); }}    
    >
      <span className="flex items-center gap-2">
        {icon} {title}
      </span>
    </li>
  );
};

export default ListItem;
