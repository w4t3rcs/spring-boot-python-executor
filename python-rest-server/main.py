import logging
import os

from fastapi import FastAPI, HTTPException, Security
from fastapi.security import APIKeyHeader
from pydantic import BaseModel

TOKEN = os.getenv("PYTHON_SERVER_TOKEN")
os.environ.pop("PYTHON_SERVER_TOKEN", None)
APPEARANCE = os.getenv("PYTHON_RESULT_APPEARANCE")
LOGGING_ENABLED = bool(os.getenv("PYTHON_LOGGING_ENABLED"))
if LOGGING_ENABLED:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(levelname)s] %(message)s",
    )

app = FastAPI()
token_header = APIKeyHeader(name="X-Token", auto_error=False)

class ScriptRequest(BaseModel):
    script: str

@app.post("/script")
async def execute_script(request: ScriptRequest,
                   api_key_header: str = Security(token_header)):
    if api_key_header != TOKEN:
        if LOGGING_ENABLED:
            logging.info(f"Client failed to connect to the server: {request}")
        raise HTTPException(401, detail="Incorrect token")
    try:
        execution_result = {}
        exec(request.script, {}, execution_result)
        if LOGGING_ENABLED:
            logging.info(f"Client executed the script: {request}")
        return execution_result.get(APPEARANCE)
    except Exception as e:
        raise HTTPException(400, detail=str(e))