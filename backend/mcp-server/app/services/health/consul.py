import requests
from app.config.settings import get_settings


def check_consul():
    try:
        r = requests.get(
            f"http://{get_settings().CONSUL_HOST}:{get_settings().CONSUL_PORT}/v1/status/leader",
            timeout=2
        )
        if r.status_code == 200 and r.text != '""':
            return {"status": "UP", "leader": r.text}
        return {"status": "DOWN"}
    except Exception as e:
        return {"status": "DOWN", "error": str(e)}
