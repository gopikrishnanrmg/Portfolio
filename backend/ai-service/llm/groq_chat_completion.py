from langchain_groq import ChatGroq


class GroqLLM:
    def __init__(self, api_key, model):
        self.__client = ChatGroq(api_key=api_key, model=model)

    def send_message(self, messages, conversation_id):
        response = self.__client.invoke(messages)
        return response.content, None
