from pydantic import BaseModel
from langchain_core.documents.base import Document

class CreateContextItemRequest(BaseModel):
    title: str
    content: str

class GetContextItemRequest(BaseModel):
    k: int
    query: str

class DeleteContextItemRequest(BaseModel):
    title: str

class CreateContextItemResponse(BaseModel):
    chunks: int

class GetcontextItemResponses(BaseModel):
    matches: list[Document]