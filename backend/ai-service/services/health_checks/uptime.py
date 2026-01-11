import time

START_TIME = time.time()

def check_uptime():
    return {
        "status": "UP",
        "details": {
            "uptime_seconds": int(time.time() - START_TIME)
        }
    }
