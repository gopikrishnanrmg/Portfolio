from fastapi import FastAPI
from contextlib import asynccontextmanager
from config.settings import ENABLE_CONSUL_REGISTRATION
from utils.consul_registration import register_with_consul
from prometheus_fastapi_instrumentator import Instrumentator
from routes.chat_controller import router as chat_router
from routes.context_controller import router as context_router
from routes.health import router as health_router

@asynccontextmanager
async def lifespan(app: FastAPI):
    if ENABLE_CONSUL_REGISTRATION:
        register_with_consul()
    yield

app = FastAPI(lifespan=lifespan)
app.include_router(chat_router)
app.include_router(context_router)
app.include_router(health_router)

Instrumentator().instrument(app).expose(app, endpoint="/actuator/prometheus")
