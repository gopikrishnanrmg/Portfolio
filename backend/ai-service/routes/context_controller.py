from fastapi import APIRouter, Depends

from dtos.context_dtos import CreateContextItemRequest, CreateContextItemResponse, GetcontextItemResponses
from security.api_key_filter import verify_admin_key
from services.context_service import add_context_item, find_k_matches, soft_delete_by_title

router = APIRouter(prefix="/v1/context")

@router.post("/", response_model=CreateContextItemResponse, dependencies=[Depends(verify_admin_key)])
async def context(item: CreateContextItemRequest):
    chunks_added = add_context_item(item)
    return CreateContextItemResponse(chunks=chunks_added)

@router.get("/", response_model=GetcontextItemResponses, dependencies=[Depends(verify_admin_key)])
async def context(query: str, k: int):
    match_list = find_k_matches(query, k)
    return GetcontextItemResponses(matches=match_list)

@router.delete("/", dependencies=[Depends(verify_admin_key)])
async def context(title: str):
    soft_delete_by_title(title)
