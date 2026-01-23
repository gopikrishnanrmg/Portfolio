from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi_mcp import FastApiMCP

from prometheus_fastapi_instrumentator import Instrumentator
from config.settings import ENABLE_CONSUL_REGISTRATION, SERVICE_NAME
from routes.health import router as health_router
from routes.mcp_server import router as mcp_server
from utils.consul_registration import register_with_consul

@asynccontextmanager
async def lifespan(app: FastAPI):
    if ENABLE_CONSUL_REGISTRATION:
        register_with_consul()
    yield

app = FastAPI(name=SERVICE_NAME, lifespan=lifespan)
app.include_router(health_router)
app.include_router(mcp_server)

mcp = FastApiMCP(app, include_operations=["send-message"])

mcp.mount_http()

Instrumentator().instrument(app).expose(app, endpoint="/actuator/prometheus")