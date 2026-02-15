import socket
import consul
from app.config.settings import get_settings

def get_lan_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        return s.getsockname()[0]
    finally:
        s.close()

def register_with_consul():
    host_ip = get_lan_ip()
    c = consul.Consul(host=get_settings().CONSUL_HOST, port=get_settings().CONSUL_PORT)
    c.agent.service.register(
        name=get_settings().SERVICE_NAME,
        service_id=get_settings().SERVICE_NAME,
        address=host_ip,
        port=get_settings().SERVICE_PORT,
        check=consul.Check.http(
            f"http://{host_ip}:{get_settings().SERVICE_PORT}/actuator/health",
            "10s"
        )
    )
