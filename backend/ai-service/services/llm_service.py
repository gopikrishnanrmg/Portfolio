import uuid
from fastapi import Request
from langchain_groq import ChatGroq

from config.settings import LLM_API_KEY, MODEL_NAME
from dtos.chat_dtos import ChatRequest

llm = ChatGroq(
    api_key=LLM_API_KEY,
    model=MODEL_NAME,
    temperature=0.7)

async def generate_reply(request: Request, body: ChatRequest):
    session_id = request.cookies.get("session_id") or str(uuid.uuid4())

    user_msg = body.content

    messages = [("system", "You are a helpful, friendly, and a very concise chatbot."),
                ("user", user_msg)]

    response = llm.invoke(messages)

    reply = response.content

    return reply, session_id
