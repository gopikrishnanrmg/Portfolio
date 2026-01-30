from fastapi import APIRouter, Depends, Response, status
from starlette.status import HTTP_201_CREATED

from app.dtos.context_dtos import CreateContextItemRequest, CreateContextItemResponse, GetcontextItemResponses
from app.security.api_key_filter import verify_admin_key
from app.services.context_service import add_context_item, find_k_matches, soft_delete_by_title

router = APIRouter(prefix="/api/v1/context")

@router.post("/", response_model=CreateContextItemResponse, status_code=HTTP_201_CREATED, dependencies=[Depends(verify_admin_key)])
async def create_context_item(item: CreateContextItemRequest):
    chunks_added = add_context_item(item)
    return CreateContextItemResponse(chunks=chunks_added)

@router.get("/", response_model=GetcontextItemResponses, dependencies=[Depends(verify_admin_key)])
async def get_context_items(query: str, k: int):
    match_list = find_k_matches(query, k)
    return GetcontextItemResponses(matches=match_list)

@router.delete("/", dependencies=[Depends(verify_admin_key)])
async def delete_context_item(title: str):
    soft_delete_by_title(title)
    return Response(status_code=status.HTTP_204_NO_CONTENT)
