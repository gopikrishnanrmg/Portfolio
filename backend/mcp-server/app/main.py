from contextlib import asynccontextmanager

from fastapi import FastAPI, Depends
from fastapi_mcp import FastApiMCP, AuthConfig
from prometheus_fastapi_instrumentator import Instrumentator

from app.config.settings import get_settings
from app.routes.health import router as health_router
from app.routes.rabbitmq import router as mcp_server
from app.security.auth import verify_auth
from app.utils.consul_registration import register_with_consul

@asynccontextmanager
async def lifespan(app: FastAPI):
    if get_settings().ENABLE_CONSUL_REGISTRATION:
        register_with_consul()
    yield

app = FastAPI(name=get_settings().SERVICE_NAME, lifespan=lifespan)
app.include_router(health_router)
app.include_router(mcp_server)

mcp = FastApiMCP(
    app, include_operations=["send-message"],
    auth_config=AuthConfig(dependencies=[Depends(verify_auth)],),)

mcp.mount_http()

Instrumentator().instrument(app).expose(app, endpoint="/actuator/prometheus")