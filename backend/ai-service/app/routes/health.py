from fastapi import APIRouter
from app.services.health_checks.aggregator import build_health_report
from app.config.settings import SERVICE_NAME

router = APIRouter()

@router.get("/actuator/health")
async def health():
    return await build_health_report()

@router.get("/actuator/info")
def info():
    return {
        "app": {
            "name": SERVICE_NAME,
            "version": "1.0.0"
        }
    }
