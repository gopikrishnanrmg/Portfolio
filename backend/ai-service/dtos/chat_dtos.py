from typing import Optional
from pydantic import BaseModel


class ChatRequest(BaseModel):
    content: str

class ChatMessage(BaseModel):
    role: str
    content: str

class ChatResponse(BaseModel):
    reply: str
    session_id: Optional[str] = None

