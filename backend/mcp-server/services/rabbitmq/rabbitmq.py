import aio_pika
import json
from aio_pika import ExchangeType
from config.settings import RABBIT_URL, EXCHANGE


async def publish_message(routing_key: str, message: dict):
    connection = await aio_pika.connect_robust(RABBIT_URL)
    async with connection:
        channel = await connection.channel()
        exchange = await channel.declare_exchange(EXCHANGE, ExchangeType.DIRECT, auto_delete=True)

        message = aio_pika.Message(body=json.dumps(message).encode("utf-8"), content_type="application/json", delivery_mode=aio_pika.DeliveryMode.PERSISTENT)

        await exchange.publish(message, routing_key=routing_key)