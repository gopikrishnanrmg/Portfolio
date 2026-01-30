import socket
import consul
from app.config.settings import SERVICE_NAME, SERVICE_PORT, CONSUL_HOST, CONSUL_PORT

def get_lan_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        return s.getsockname()[0]
    finally:
        s.close()

def register_with_consul():
    host_ip = get_lan_ip()
    c = consul.Consul(host=CONSUL_HOST, port=CONSUL_PORT)
    c.agent.service.register(
        name=SERVICE_NAME,
        service_id=SERVICE_NAME,
        address=host_ip,
        port=SERVICE_PORT,
        check=consul.Check.http(
            f"http://{host_ip}:{SERVICE_PORT}/actuator/health",
            "10s"
        )
    )
