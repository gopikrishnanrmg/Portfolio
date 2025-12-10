import React from 'react'

const Footer = () => {
    return (
        <footer className="w-full py-6 mt-8 text-center text-xs md:text-sm text-gray-400 border-t border-gray-700/50">
            <div className="mx-auto w-full max-w-6xl lg:max-w-7xl xl:max-w-[1400px] px-8">
              <p>
                  Copyleft © {new Date().getFullYear()} Gopikrishnan Rajeev.
                  This site is licensed under the&nbsp;
                  <a
                      href="https://www.gnu.org/licenses/gpl-3.0.html"
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-cyan-400 hover:underline"
                  >
                      GNU GPL v3
                  </a>.
              </p>
              <p className="mt-2">
                  3D model credit:<span> </span>
                  <a
                      href="https://sketchfab.com/3d-models/lowpoly-island-26c3f2f271ab41a5a0c9178ac5304df8"
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-cyan-400 hover:underline"
                  >
                      alfance
                  </a>
                  (licensed under CC BY 4.0).
              </p>
            </div>
        </footer>

    )
}

export default Footer
