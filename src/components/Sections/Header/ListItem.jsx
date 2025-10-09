import React from 'react'

const ListItem = (props) => {
  return (
    <li className={`${props.mobile?'active:bg-cyan-800 px-2':'hover:bg-cyan-800 rounded-2xl px-2'}`}>
        <a href={props.id}>{props.title}</a>
    </li>
  )
}

export default ListItem
