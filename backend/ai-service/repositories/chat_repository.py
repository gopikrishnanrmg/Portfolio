from config.db import SessionLocal
from models.chat_models import ChatSession, ChatMessage

def get_db():
    return SessionLocal()

def get_session(db, session_id: str):
    return db.query(ChatSession).filter_by(session_id=session_id).first()

def create_session(db, session_id: str):
    session = ChatSession(session_id=session_id)
    db.add(session)
    db.commit()
    return session

def get_history(db, session_id: str, limit: int = 10):
    history = (
        db.query(ChatMessage)
        .filter_by(session_id=session_id)
        .order_by(ChatMessage.id.desc())
        .limit(limit)
        .all()
    )
    return list(reversed(history))

def save_message(db, session_id: str, role: str, content: str):
    msg = ChatMessage(
        session_id=session_id,
        role=role,
        content=content
    )
    db.add(msg)
    db.commit()
    return msg
