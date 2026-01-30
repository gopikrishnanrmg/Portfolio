from fastapi import Header, HTTPException
from starlette import status

from app.config.settings import get_settings


def verify_admin_key(x_api_key: str = Header(None)):
    if x_api_key != get_settings().ADMIN_API_KEY:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid or missing admin key")