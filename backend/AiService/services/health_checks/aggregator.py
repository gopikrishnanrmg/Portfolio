from .disk import check_disk_space
from .memory import check_memory
from .consul import check_consul
from .uptime import check_uptime
from .database import check_database
from .ping import check_ping

async def build_health_report():
    return {
        "status": "UP",
        "components": {
            "ping": check_ping(),
            "diskSpace": check_disk_space(),
            "memory": check_memory(),
            "consul": check_consul(),
            #"database": await check_database(),
            "uptime": check_uptime(),
        }
    }
