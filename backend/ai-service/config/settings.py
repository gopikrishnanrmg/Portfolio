import os

SERVICE_NAME = os.getenv("SERVICE_NAME", "ai-service")
SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8885"))
ENABLE_CONSUL_REGISTRATION = os.getenv("ENABLE_CONSUL_REGISTRATION", "false").lower() == "true"
CONSUL_HOST = os.getenv("CONSUL_SERVER_HOST", "localhost")
CONSUL_PORT = int(os.getenv("CONSUL_SERVER_PORT", "8500"))
LLM_API_KEY = os.getenv("LLM_API_KEY")
MODEL_NAME = os.getenv("MODEL_NAME")
DB_USER=os.getenv("DB_USER")
DB_PASSWORD=os.getenv("DB_PASSWORD")
DB_HOST=os.getenv("DB_HOST")
DB_PORT=os.getenv("DB_PORT")
DB_NAME = os.getenv("DB_NAME")
ADMIN_API_KEY = os.getenv("ADMIN_API_KEY")

DATABASE_URL = f"postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"