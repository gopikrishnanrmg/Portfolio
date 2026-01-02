import React from 'react'

const CookieBanner = ({onAccept, onReject}) => {
  return (
    <div className="fixed bottom-4 left-4 bg-gray-900 text-cyan-400 border border-cyan-500 p-4 rounded-lg shadow-lg flex flex-col gap-3 max-w-xs z-50">
      <div>Are you willing to accept cookies for anonymous data collection?</div>

      <div className="flex gap-2 justify-end">
        <button className="px-3 py-1 bg-cyan-600 text-white rounded hover:bg-cyan-700" onClick={onAccept}>
          Accept
        </button>
        <button className="px-3 py-1 bg-gray-700 text-white rounded hover:bg-gray-800" onClick={onReject}>
          Reject
        </button>
      </div>
    </div>
  )
}

export default CookieBanner
