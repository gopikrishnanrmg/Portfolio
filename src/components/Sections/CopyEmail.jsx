import { MdEmail } from 'react-icons/md'

const CopyEmail = () => {
  const copyEmail = () => {
    navigator.clipboard.writeText('gopikrishnan.rmg@outlook.com')
    alert('Email copied to clipboard!')
  }

  return (
    <button
      onClick={copyEmail}
      className="hover:text-cyan-500"
      title="Copy email"
    >
      <MdEmail />
    </button>
  )
}

export default CopyEmail
