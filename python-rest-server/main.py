from fastapi import FastAPI, HTTPException
from fastapi.params import Header
from pydantic import BaseModel
import os

app = FastAPI()

USERNAME = os.getenv("PYTHON_SERVER_USERNAME")
PASSWORD = os.getenv("PYTHON_SERVER_PASSWORD")
APPEARANCE = os.getenv("PYTHON_RESULT_APPEARANCE")

class ScriptRequest(BaseModel):
    script: str

@app.post("/script")
def execute_script(request: ScriptRequest,
                   username: str = Header(..., alias="X-Username", convert_underscores=False),
                   password: str = Header(..., alias="X-Password", convert_underscores=False)):
    if (username != USERNAME) or (password != PASSWORD):
        raise HTTPException(401, detail="Incorrect username or password")
    try:
        execution_result = {}
        exec(request.script, {}, execution_result)
        return execution_result.get(APPEARANCE)
    except Exception as e:
        raise HTTPException(400, detail=str(e))