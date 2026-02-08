import time

import aio_pika
import json
from aio_pika import ExchangeType
from app.config.settings import get_settings


async def publish_message(routing_key: str, message: dict):
    connection = await aio_pika.connect_robust(get_settings().get_queue_url)
    if "timestamp" not in message:
        message["timestamp"] = int(time.time() * 1000)
    async with connection:
        channel = await connection.channel()
        exchange = await channel.declare_exchange(get_settings().EXCHANGE, ExchangeType.DIRECT, auto_delete=False)

        queue = await channel.declare_queue(get_settings().QUEUE, durable=True)
        await queue.bind(exchange, routing_key=routing_key)

        message = aio_pika.Message(body=json.dumps(message).encode("utf-8"), content_type="application/json", delivery_mode=aio_pika.DeliveryMode.PERSISTENT)
        await exchange.publish(message, routing_key=routing_key)