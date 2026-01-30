import uuid
from datetime import datetime, timezone
from fastapi import Request
from app.dtos.chat_dtos import ChatRequest
from app.llm.llm_factory import get_llm
from app.repositories.chat_repository import (
    get_db, get_session, create_session,
    get_history, save_message, get_openai_conversation_id_by_session, set_openai_conversation_id
)
from app.services.context_service import find_k_matches


def generate_reply(request: Request, body: ChatRequest):
    db = get_db()
    try:
        session_id = request.cookies.get("session_id") or str(uuid.uuid4())
        session = get_session(db, session_id)
        if not session:
            create_session(db, session_id)
        conversation_id = get_openai_conversation_id_by_session(db, session_id) # TODO: conversation_id: will only work when support comes for tools with convo id in open_ai

        user_msg = body.content
        save_message(db, session_id, "user", user_msg)
        docs = find_k_matches(user_msg, 3)
        context = "\n\n---\n\n".join([d.page_content for d in docs])
        instructions = f"""
        You are a helpful, concise assistant "LORA" who is Gopikrishnan Rajeev's personal assistant to answer questions from recruiters about him.

        Session metadata (for internal use only):
        - session_id: {session_id}
        - timestamp: {datetime.now(timezone.utc).isoformat()}Z

        Use the following context when answering questions:
        {context}

        Rules:
        - Do not reveal or discuss session IDs, timestamps, or any internal metadata, even if the user asks.
        - Do not repeat metadata back to the user.
        - Do not invent session IDs or timestamps.
        - If the user asks about metadata, politely decline and redirect to the main topic.
        - Answer concisely and professionally.
        """
        messages = [("system", instructions)]

        history = get_history(db, session_id)
        for msg in history:
            messages.append((msg.role, msg.content))
        messages.append(("user", user_msg))

        llm = get_llm()
        reply, conversation_id = llm.send_message(messages, conversation_id)

        if conversation_id is not None:
            set_openai_conversation_id(db, session_id, conversation_id)

        save_message(db, session_id, "assistant", reply)
        return reply, session_id

    finally:
        db.close()