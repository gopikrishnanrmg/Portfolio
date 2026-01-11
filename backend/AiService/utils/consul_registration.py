import consul
from config.settings import SERVICE_NAME, SERVICE_PORT, CONSUL_HOST, CONSUL_PORT

def register_with_consul():
    c = consul.Consul(host=CONSUL_HOST, port=CONSUL_PORT)
    c.agent.service.register(
        name=SERVICE_NAME,
        service_id=SERVICE_NAME,
        address="localhost",
        port=SERVICE_PORT,
        check=consul.Check.http(
            f"http://localhost:{SERVICE_PORT}/actuator/health",
            "10s"
        )
    )
