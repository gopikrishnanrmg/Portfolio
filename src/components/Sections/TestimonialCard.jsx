import React from 'react'

const TestimonialCard = ({ text, initials, accent, name, role }) => {
    return (
        <div
            className="testimonial-card flex flex-col h-full
                 rounded-2xl backdrop-blur-2xl bg-black/40 border border-gray-700
                 shadow-lg ring-1 ring-white/20 hover:border-cyan-500 hover:shadow-cyan-500/30
                 transition-all duration-300 p-6 relative"
        >
            <div className="w-full flex justify-center mb-4">
                <span
                    className="text-6xl md:text-7xl font-bold leading-none
                     bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
                     bg-clip-text text-transparent select-none"
                >
                    “
                </span>
            </div>

            <p className="italic text-gray-200 mb-6 flex-1">{text}</p>

            <div className="flex items-center gap-4 mt-auto">
                <div
                    className={`w-12 h-12 rounded-full flex items-center justify-center 
                      text-white font-bold bg-gradient-to-r ${accent} flex-shrink-0`}
                >
                    {initials}
                </div>

                <div className="min-w-0">
                    <p
                        className="testimonial-name font-semibold
             bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-400
             bg-clip-text text-transparent
             [background-size:300%_300%] [background-position:0%_center]"
                    >
                        {name}
                    </p>


                    <p className="text-xs text-gray-400 break-words">{role}</p>
                </div>
            </div>
        </div>
    )
}

export default TestimonialCard
