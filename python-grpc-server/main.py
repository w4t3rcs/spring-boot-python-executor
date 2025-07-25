import os
from concurrent import futures
import grpc
import python_pb2
import python_pb2_grpc
import logging
import json

USERNAME = os.getenv("PYTHON_SERVER_USERNAME")
PASSWORD = os.getenv("PYTHON_SERVER_PASSWORD")
HOST = os.getenv("PYTHON_SERVER_HOST")
PORT = os.getenv("PYTHON_SERVER_PORT")
MAX_WORKERS = int(os.getenv("PYTHON_SERVER_THREAD_POOL_MAX_WORKERS"))
APPEARANCE = os.getenv("PYTHON_RESULT_APPEARANCE")

logging.basicConfig(level=logging.INFO)

class PythonService(python_pb2_grpc.PythonServiceServicer):
    def SendCode(self, request, context):
        script = request.script
        meta = dict(context.invocation_metadata())
        if meta.get("X-Username") != USERNAME or meta.get("X-Password") != PASSWORD:
            context.set_code(grpc.StatusCode.PERMISSION_DENIED)
            context.set_details("Invalid credentials")
            return python_pb2.ScriptResponse(result="")
        try:
            execution_result = {}
            exec(script, {}, execution_result)
            return python_pb2.ScriptResponse(result=json.dumps(execution_result.get(APPEARANCE)))
        except Exception as e:
            context.set_details(str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            return python_pb2.ScriptResponse(result="")

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=MAX_WORKERS))
    python_pb2_grpc.add_PythonServiceServicer_to_server(PythonService(), server)
    server.add_insecure_port(HOST + ':' + str(PORT))
    server.start()
    logging.info(f"gRPC server running at {HOST}:{PORT}")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()