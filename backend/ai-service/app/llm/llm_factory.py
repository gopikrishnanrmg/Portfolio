from app.config.settings import get_settings
from app.llm.groq_chat_completion import GroqLLM
from app.llm.openai_chat_response import OpenAILLM

def get_llm():
    if get_settings().API_SERVICE_NAME == "groq":
        return GroqLLM(api_key=get_settings().LLM_API_KEY, model=get_settings().MODEL_NAME)
    else:
        return OpenAILLM(api_key=get_settings().LLM_API_KEY, model=get_settings().MODEL_NAME, mcp_url=get_settings().MCP_URL)
