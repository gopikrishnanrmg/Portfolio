import shutil

def check_disk_space():
    total, used, free = shutil.disk_usage("/")
    threshold = 10 * 1024 * 1024
    return {
        "status": "UP" if free > threshold else "DOWN",
        "details": {"total": total, "free": free}
    }
