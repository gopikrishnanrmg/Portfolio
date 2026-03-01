import React, { useState, useEffect, useMemo } from "react";
import { createPortal } from "react-dom";
import ListItem from "./ListItem";
import Hamburger from "./Hamburger";
import {
  FaUser,
  FaTools,
  FaBriefcase,
  FaCode,
  FaComments,
  FaJedi,
} from "react-icons/fa";
import { IoMdAnalytics } from "react-icons/io";

const NavBar = () => {
  const [open, setOpen] = useState(false);
  const [active, setActive] = useState("#hero");
  const [grafanaUrl, setGrafanaUrl] = useState(null);
  
  useEffect(() => {
    setGrafanaUrl(window.RUNTIME_CONFIG?.GRAFANA_URL || null);
  }, []);

const sections = useMemo(() => [
  { id: "#hero", title: "Introduction", icon: <FaUser />, type: "scroll" },
  { id: "#features", title: "Features", icon: <FaJedi />, type: "scroll" },
  { id: "#skills", title: "Skills", icon: <FaTools />, type: "scroll" },
  {
    id: "#work-experience",
    title: "Work Experience",
    icon: <FaBriefcase />,
    type: "scroll",
  },
  { id: "#projects", title: "Projects", icon: <FaCode />, type: "scroll" },
  {
    id: "#testimonials",
    title: "Testimonials",
    icon: <FaComments />,
    type: "scroll",
  },
  grafanaUrl && {
    id: `${grafanaUrl}dashboards`,
    title: "Analytics",
    icon: <IoMdAnalytics />,
    type: "external",
  }
].filter(Boolean), [grafanaUrl]);



  const toggleMenu = () => setOpen(!open);

  useEffect(() => {
    document.body.style.overflow = open ? "hidden" : "auto";
  }, [open]);

  useEffect(() => {
    let lastUpdate = 0;
    const scrollHandler = () => {
      const now = Date.now();
      if (now - lastUpdate < 100) return;
      lastUpdate = now;

      const scrollPos = window.scrollY + window.innerHeight / 2;
      let current = sections[0].id;

      sections.forEach((s) => {
        if (s.type === "scroll") {
          const el = document.querySelector(s.id);
          if (el) {
            const offsetTop = el.offsetTop;
            const offsetBottom = offsetTop + el.offsetHeight;
            if (scrollPos >= offsetTop && scrollPos < offsetBottom) {
              current = s.id;
            }
          }
        }
      });

      setActive(current);
    };

    window.addEventListener("scroll", scrollHandler);
    scrollHandler();

    return () => window.removeEventListener("scroll", scrollHandler);
  }, [sections]);

  return (
    <>
      <nav className="hidden md:flex">
        <ul className="flex space-x-8 lg:space-x-12 text-white text-lg">
          {sections.map((s) => (
            <ListItem
              key={s.id}
              id={s.id}
              title={s.title}
              icon={s.icon}
              active={active}
              type={s.type}
            />
          ))}
        </ul>
      </nav>

      <Hamburger open={open} toggle={toggleMenu} />

      {createPortal(
        <div
          className={`fixed inset-0 h-screen overflow-hidden touch-none backdrop-blur-xl text-white p-6 z-40 transition-all duration-300 ease-in-out
            ${
              open
                ? "translate-y-0 opacity-100 pointer-events-auto"
                : "-translate-y-full opacity-0 pointer-events-none"
            }`}
        >
          <ul className="flex flex-col gap-6 text-lg mt-20">
            {sections.map((s) => (
              <ListItem
                key={s.id}
                id={s.id}
                title={s.title}
                icon={s.icon}
                active={active}
                type={s.type}
                onClick={() => setOpen(false)}
              />
            ))}
          </ul>
        </div>,
        document.body
      )}
    </>
  );
};

export default NavBar;
