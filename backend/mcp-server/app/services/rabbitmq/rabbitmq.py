import time

import aio_pika
import json
from aio_pika import ExchangeType
from app.config.settings import RABBIT_URL, EXCHANGE, QUEUE


async def publish_message(routing_key: str, message: dict):
    connection = await aio_pika.connect_robust(RABBIT_URL)
    if "timestamp" not in message:
        message["timestamp"] = int(time.time() * 1000)

    async with connection:
        channel = await connection.channel()
        exchange = await channel.declare_exchange(EXCHANGE, ExchangeType.DIRECT, auto_delete=False)

        queue = await channel.declare_queue(QUEUE, durable=True)
        await queue.bind(exchange, routing_key=routing_key)

        message = aio_pika.Message(body=json.dumps(message).encode("utf-8"), content_type="application/json", delivery_mode=aio_pika.DeliveryMode.PERSISTENT)

        await exchange.publish(message, routing_key=routing_key)