from fastapi import APIRouter
from config.settings import SERVICE_NAME

router = APIRouter()

@router.get("/actuator/health")
def health():
    return {"status": "UP"}

@router.get("/actuator/info")
def info():
    return {
        "app": {
            "name": SERVICE_NAME,
            "version": "1.0.0"
        }
    }
