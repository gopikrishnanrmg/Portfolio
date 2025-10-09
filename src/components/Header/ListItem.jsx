import React from 'react'

const ListItem = (props) => {
  return (
    <li className={`${props.mobile?'active:bg-cyan-700 px-2':'hover:bg-cyan-700 rounded-2xl px-2'}`}>
        <a href={props.id}>{props.title}</a>
    </li>
  )
}

export default ListItem
