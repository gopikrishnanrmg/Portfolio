from typing import Protocol, Any


class LLM(Protocol):
    def send_message(self, message: Any, conversation_id: Any) -> Any:
        ...