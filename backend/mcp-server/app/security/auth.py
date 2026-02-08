from fastapi import HTTPException, Depends, Request
from fastapi.security import HTTPBearer

from app.config.settings import get_settings

token_auth = HTTPBearer()

async def verify_auth(token=Depends(token_auth)):
    if token.credentials != get_settings().ADMIN_API_KEY:
        raise HTTPException(status_code=401, detail="Invalid token")

