FROM python:3.13-slim
LABEL authors="gopikrishnanrmg"
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 8884
CMD ["uvicorn", "main:app", "--reload", "--host", "0.0.0.0", "--port", "8884"]
