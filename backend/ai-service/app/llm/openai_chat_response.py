from openai import OpenAI

from app.config.settings import get_settings


class OpenAILLM:
    def __init__(self, api_key, model, mcp_url):
        self.__client = OpenAI(api_key=api_key)
        self.__model = model
        self.__mcp_url = mcp_url

    def send_message(self, messages, conversation_id):
        instructions = messages[0][1]

        history_items = []
        for role, content in messages[1:]:
            history_items.append({"role": role, "content": content})

        response = self.__client.responses.create(
            model=self.__model,
            conversation=conversation_id,
            instructions=instructions,
            tools=[
                {
                    "type": "mcp",
                    "server_label": "rabbitmq",
                    "server_description": "A service to send data to rabbitmq.",
                    "server_url": self.__mcp_url,
                    "require_approval": "never",
                    "headers": {
                        "Authorization": f"Bearer {get_settings().ADMIN_API_KEY}"
                    }
                },
            ],
            input=history_items,
        )

        return response.output_text, None #TODO: Later we will return this as chatresponse
