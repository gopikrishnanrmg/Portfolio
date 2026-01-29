import requests
from app.config.settings import CONSUL_HOST, CONSUL_PORT

def check_consul():
    try:
        r = requests.get(
            f"http://{CONSUL_HOST}:{CONSUL_PORT}/v1/status/leader",
            timeout=2
        )
        if r.status_code == 200 and r.text != '""':
            return {"status": "UP", "leader": r.text}
        return {"status": "DOWN"}
    except Exception as e:
        return {"status": "DOWN", "error": str(e)}
