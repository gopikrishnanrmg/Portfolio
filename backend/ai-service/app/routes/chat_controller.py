from fastapi import APIRouter, Request, Response
from app.dtos.chat_dtos import ChatRequest, ChatResponse
from app.services.chat_service import generate_reply
from app.config.settings import get_settings
from typing import Literal
router = APIRouter(prefix="/api/v1/chat")

@router.post("", response_model=ChatResponse)
async def chat(request: Request, body: ChatRequest, response: Response):
    reply, session_id = generate_reply(request, body)

    SameSiteType = Literal["lax", "strict", "none"]
    if get_settings().profile == "prod":
        same_site: SameSiteType = "none"
        secure = True
    else:
        same_site: SameSiteType ="lax"
        secure = False

    if not request.cookies.get("session_id"):
        response.set_cookie(
            key="session_id",
            value=session_id,
            httponly=True,
            samesite=same_site,
            secure=secure,
            max_age=60*60*24*30
        )

    return ChatResponse(reply=reply, session_id=session_id)
