import React, { useState } from 'react'
import { MdEmail } from 'react-icons/md'
import { QRCodeSVG } from 'qrcode.react'
import { FaCheckCircle } from 'react-icons/fa';

const EmailWithQR = () => {
    const [showQR, setShowQR] = useState(false)
    const [copied, setCopied] = useState(false)
    const email = 'gopikrishnan.rmg@outlook.com'

    const handleClick = () => {
        navigator.clipboard.writeText(email)
        setCopied(true)
        setTimeout(() => setCopied(false), 1500)

        setShowQR(true)
    }

    return (
        <div className="relative inline-flex">
            <button
                onClick={handleClick}
                className="hover:text-cyan-500"
                title="Copy email & show QR"
            >
                <MdEmail />
            </button>

            {copied && (
                <span className="absolute -top-6 left-1/2 -translate-x-1/2 text-xs text-cyan-400">
                    Copied!
                </span>
            )}

            {showQR && (
                <div className="fixed inset-0 flex items-center justify-center bg-black/60 z-50">
                    <div className="bg-black/90 p-6 rounded-2xl shadow-2xl border border-cyan-500 
                    flex flex-col items-center text-center max-w-sm w-full mx-4">
                        <div className="flex items-center justify-center mb-4 text-cyan-400">
                            <FaCheckCircle className="mr-2" />
                            <span className="text-base font-semibold">Email copied!</span>
                        </div>
                        <p className="mb-6 text-sm text-gray-300 leading-relaxed">
                            Alternatively, scan this QR code with your phone to send me an email from your device.
                        </p>
                        <QRCodeSVG
                            value={`mailto:${email}`}
                            size={180}
                            fgColor="#22d3ee"
                            bgColor="transparent"
                        />
                        <button
                            onClick={() => setShowQR(false)}
                            className="mt-6 px-4 py-1.5 rounded-md text-sm font-medium 
                   text-cyan-400 border border-cyan-500 hover:bg-cyan-500/20 
                   transition-colors"
                        >
                            Close
                        </button>
                    </div>
                </div>
            )}

        </div>
    )
}

export default EmailWithQR
