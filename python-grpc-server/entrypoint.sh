#!/bin/sh

python install.py
pip install --no-cache-dir -r requirements.txt
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. python.proto
python main.py