from app.config.settings import API_SERVICE_NAME, LLM_API_KEY, MODEL_NAME, MCP_URL
from app.llm.groq_chat_completion import GroqLLM
from app.llm.openai_chat_response import OpenAILLM

def get_llm():
    if API_SERVICE_NAME == "groq":
        return GroqLLM(api_key=LLM_API_KEY, model=MODEL_NAME)
    else:
        return OpenAILLM(api_key=LLM_API_KEY, model=MODEL_NAME, mcp_url=MCP_URL)
