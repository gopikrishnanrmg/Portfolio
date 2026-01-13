from fastapi import APIRouter
from dtos.context_dtos import CreateContextItemRequest, CreateContextItemResponse, GetContextItemRequest, \
    GetcontextItemResponses, DeleteContextItemRequest
from services.context_service import add_context_item, find_k_matches, soft_delete_by_title

router = APIRouter(prefix="/v1/context")

@router.post("/", response_model=CreateContextItemResponse)
async def context(item: CreateContextItemRequest):
    chunks_added = add_context_item(item)
    return CreateContextItemResponse(chunks=chunks_added)

@router.get("/", response_model=GetcontextItemResponses)
async def context(query: str, k: int):
    match_list = find_k_matches(query, k)
    return GetcontextItemResponses(matches=match_list)

@router.delete("/")
async def context(title: str):
    soft_delete_by_title(title)
