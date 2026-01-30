import os

from functools import lru_cache


class Settings:
    def __init__(self):
        self.SERVICE_NAME = os.getenv("SERVICE_NAME", "ai-service")
        self.SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8885"))
        self.ENABLE_CONSUL_REGISTRATION = os.getenv("ENABLE_CONSUL_REGISTRATION", "false").lower() == "true"
        self.CONSUL_HOST = os.getenv("CONSUL_HOST", "localhost")
        self.CONSUL_PORT = int(os.getenv("CONSUL_PORT", "8500"))
        self.LLM_API_KEY = os.getenv("LLM_API_KEY")
        self.MODEL_NAME = os.getenv("MODEL_NAME")
        self.MCP_URL = os.getenv("MCP_URL")
        self.DB_USER=os.getenv("DB_USER")
        self.DB_PASSWORD=os.getenv("DB_PASSWORD")
        self.DB_HOST=os.getenv("DB_HOST")
        self.DB_PORT=os.getenv("DB_PORT")
        self.DB_NAME = os.getenv("DB_NAME")
        self.ADMIN_API_KEY = os.getenv("ADMIN_API_KEY")
        self.API_SERVICE_NAME = os.getenv("API_SERVICE_NAME", "openai")

    @property
    def get_db_url(self):
        return f"postgresql://{self.DB_USER}:{self.DB_PASSWORD}@{self.DB_HOST}:{self.DB_PORT}/{self.DB_NAME}"

@lru_cache
def get_settings():
    return Settings()