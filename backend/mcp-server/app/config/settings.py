import os
from functools import lru_cache


class Settings:
    def __init__(self):
        self.SERVICE_NAME = os.getenv("SERVICE_NAME", "mcp-server")
        self.SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8884"))
        self.ENABLE_CONSUL_REGISTRATION = os.getenv("ENABLE_CONSUL_REGISTRATION", "false").lower() == "true"
        self.CONSUL_HOST = os.getenv("CONSUL_HOST", "localhost")
        self.CONSUL_PORT = int(os.getenv("CONSUL_PORT", "8500"))
        self.RABBIT_HOST = os.getenv("RABBIT_HOST", "localhost")
        self.RABBIT_PORT = int(os.getenv("RABBIT_PORT", "5672"))
        self.RABBIT_USER = os.getenv("RABBIT_USER", "guest")
        self.RABBIT_PASSWORD = os.getenv("RABBIT_PASSWORD", "")
        self.EXCHANGE = os.getenv("EXCHANGE", "messages")
        self.QUEUE = os.getenv("QUEUE", "notification")
        self.ROUTING_KEY = os.getenv("ROUTING_KEY", "messages")
        self.ADMIN_API_KEY = os.getenv("ADMIN_API_KEY", "")

    @property
    def get_queue_url(self):
        return f"amqp://{self.RABBIT_USER}:{self.RABBIT_PASSWORD}@{self.RABBIT_HOST}:{self.RABBIT_PORT}"

@lru_cache
def get_settings():
    return Settings()