import os

SERVICE_NAME = os.getenv("SERVICE_NAME", "mcp-server")
SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8884"))
ENABLE_CONSUL_REGISTRATION = os.getenv("ENABLE_CONSUL_REGISTRATION", "false").lower() == "true"
CONSUL_HOST = os.getenv("CONSUL_HOST", "localhost")
CONSUL_PORT = int(os.getenv("CONSUL_PORT", "8500"))
RABBIT_HOST = os.getenv("RABBIT_HOST", "localhost")
RABBIT_PORT = int(os.getenv("RABBIT_PORT", "5672"))
RABBIT_USER = os.getenv("RABBIT_USER", "guest")
RABBIT_PASSWORD = os.getenv("RABBIT_PASSWORD", "")
EXCHANGE = os.getenv("EXCHANGE", "messages")
ROUTING_KEY = os.getenv("ROUTING_KEY", "messages")

RABBIT_URL = f"amqp://{RABBIT_USER}:{RABBIT_PASSWORD}@{RABBIT_HOST}:{RABBIT_PORT}"
