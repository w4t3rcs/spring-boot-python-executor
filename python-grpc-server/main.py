import json
import logging
import os
from concurrent import futures

import grpc

import python_pb2
import python_pb2_grpc

TOKEN = os.getenv("PYTHON_SERVER_TOKEN")
os.environ.pop("PYTHON_SERVER_TOKEN", None)
HOST = os.getenv("PYTHON_SERVER_HOST")
PORT = os.getenv("PYTHON_SERVER_PORT")
MAX_WORKERS = int(os.getenv("PYTHON_SERVER_THREAD_POOL_MAX_WORKERS"))
APPEARANCE = os.getenv("PYTHON_RESULT_APPEARANCE")
LOGGING_ENABLED = bool(os.getenv("PYTHON_LOGGING_ENABLED"))
if LOGGING_ENABLED:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(levelname)s] %(message)s",
    )

class PythonService(python_pb2_grpc.PythonServiceServicer):
    def SendCode(self, request, context):
        if LOGGING_ENABLED:
            logging.info(f"Client wants to execute script: {request}")
        script = request.script
        meta = dict(context.invocation_metadata())
        if meta.get("x-token") != TOKEN:
            if LOGGING_ENABLED:
                logging.info(f"Client failed to connect to the server: {request}")
            context.set_code(grpc.StatusCode.PERMISSION_DENIED)
            context.set_details("Invalid credentials")
            return python_pb2.PythonResponse(result="")
        try:
            java_execution_context = {}
            exec(script, java_execution_context, java_execution_context)
            if LOGGING_ENABLED:
                logging.info(f"Client executed the script: {request}")
            return python_pb2.PythonResponse(result=json.dumps(java_execution_context.get(APPEARANCE)))
        except Exception as e:
            if LOGGING_ENABLED:
                logging.info(f"Client failed to execute the script: {request}, {str(e)}")
            context.set_details(str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            return python_pb2.PythonResponse(result="")

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=MAX_WORKERS))
    python_pb2_grpc.add_PythonServiceServicer_to_server(PythonService(), server)
    server.add_insecure_port(HOST + ':' + str(PORT))
    server.start()
    logging.info(f"gRPC server running at {HOST}:{PORT}")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()