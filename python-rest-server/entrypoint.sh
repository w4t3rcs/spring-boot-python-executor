#!/bin/sh

: "${PYTHON_SERVER_HOST:=0.0.0.0}"
: "${PYTHON_SERVER_PORT:=8000}"

python install.py
pip install --no-cache-dir -r requirements.txt
uvicorn main:app --host "$PYTHON_SERVER_HOST" --port "$PYTHON_SERVER_PORT"