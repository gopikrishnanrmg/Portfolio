import React, { useState } from 'react'
import { MdEmail } from 'react-icons/md'
import { QRCodeSVG } from 'qrcode.react'

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
                    <div className="bg-black/80 p-6 rounded-xl shadow-lg border border-cyan-500">
                        <QRCodeSVG
                            value={`mailto:${email}`}
                            size={180}
                            fgColor="#22d3ee"
                            bgColor="transparent"
                        />
                        <button
                            onClick={() => setShowQR(false)}
                            className="mt-4 block mx-auto text-sm text-gray-300 hover:text-cyan-400"
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
