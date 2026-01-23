from fastapi import APIRouter

from config.settings import ROUTING_KEY
from schemas import Message
from services.rabbitmq.rabbitmq import publish_message

router = APIRouter()
# router = APIRouter(prefix="/mcp")

@router.post("/", operation_id="send-message")
async def send_message(request: Message):
    """Send a message to RabbitMQ"""
    try:
        await publish_message(ROUTING_KEY, request.model_dump())
        return {"status": "published"}
    except Exception as e:
        return {"error": "publish_failed", "message": str(e)}
