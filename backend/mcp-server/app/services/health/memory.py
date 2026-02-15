import psutil

def check_memory():
    mem = psutil.virtual_memory()
    return {
        "status": "UP" if mem.available > 100*1024*1024 else "DOWN",
        "details": {"available": mem.available, "percent": mem.percent}
    }
