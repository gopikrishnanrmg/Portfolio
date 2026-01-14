import uuid
from fastapi import Request
from langchain_groq import ChatGroq

from config.settings import LLM_API_KEY, MODEL_NAME
from dtos.chat_dtos import ChatRequest
from repositories.chat_repository import (
    get_db, get_session, create_session,
    get_history, save_message
)
from services.context_service import find_k_matches

llm = ChatGroq(
    api_key=LLM_API_KEY,
    model=MODEL_NAME,
    temperature=0.7
)

async def generate_reply(request: Request, body: ChatRequest):
    db = get_db()

    user_msg = body.content
    docs = find_k_matches(user_msg, 3)
    context = "\n\n---\n\n".join([d.page_content for d in docs])
    messages = [("system", f"You are a helpful, friendly, and a very concise chatbot impersonating Gopikrishnan Rajeev. Use the following context about you when answering questions: {context}")]

    session_id = request.cookies.get("session_id") or str(uuid.uuid4())

    session = get_session(db, session_id)

    if not session:
        create_session(db, session_id)
    else:
        history = get_history(db, session_id)
        for msg in history:
            messages.append((msg.role, msg.content))

    messages.append(("user", user_msg))

    save_message(db, session_id, "user", user_msg)

    response = llm.invoke(messages)
    reply = response.content

    save_message(db, session_id, "assistant", reply)

    db.close()

    return reply, session_id
