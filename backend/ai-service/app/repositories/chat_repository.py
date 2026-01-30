from app.config.db import SessionLocal
from app.models.chat_models import ChatSession, ChatMessage

def get_db():
    return SessionLocal()

def get_session(db, session_id: str):
    return db.query(ChatSession).filter_by(session_id=session_id).first()

def get_openai_conversation_id_by_session(db, session_id: str):
    session = db.query(ChatSession).filter_by(session_id=session_id).first()
    return session.openai_conversation_id if session else None

def get_history(db, session_id: str, limit: int = 10):
    history = (
        db.query(ChatMessage)
        .filter_by(session_id=session_id)
        .order_by(ChatMessage.id.desc())
        .limit(limit)
        .all()
    )
    return list(reversed(history))

def set_openai_conversation_id(db, session_id: str, conversation_id: str):
    session = db.query(ChatSession).filter_by(session_id=session_id).first()
    if not session:
        raise ValueError(f"ChatSession {session_id} does not exist")

    session.openai_conversation_id = conversation_id
    db.commit()
    return session

def create_session(db, session_id: str):
    session = ChatSession(session_id=session_id)
    db.add(session)
    db.commit()
    return session

def save_message(db, session_id: str, role: str, content: str):
    msg = ChatMessage(
        session_id=session_id,
        role=role,
        content=content
    )
    db.add(msg)
    db.commit()
    return msg
