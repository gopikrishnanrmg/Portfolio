from pydantic import BaseModel, Field

class Message(BaseModel):
    session_id: str = Field(..., min_length=1)
    title: str = Field(..., min_length=1)
    message: str = Field(..., min_length=1)