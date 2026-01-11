from fastapi import APIRouter, Request, Response
from dtos.chat_dtos import ChatRequest, ChatResponse
from services.llm_service import generate_reply

router = APIRouter()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: Request, body: ChatRequest, response: Response):
    reply, session_id = await generate_reply(request, body)

    if not request.cookies.get("session_id"):
        response.set_cookie(
            key="session_id",
            value=session_id,
            httponly=True,
            samesite="Lax",
            max_age=60*60*24*30
        )

    return ChatResponse(reply=reply, session_id=session_id)
