FROM python:3.11-slim
ENV PYTHONUNBUFFERED=1
ENV PYTHON_SERVER_HOST="0.0.0.0"
ENV PYTHON_SERVER_PORT="50051"
ENV PYTHON_SERVER_THREAD_POOL_MAX_WORKERS="10"
ENV PYTHON_ADDITIONAL_IMPORTS=""
ENV PYTHON_ADDITIONAL_IMPORTS_DELIMITER=","
ENV PYTHON_RESULT_APPEARANCE="r4java"
WORKDIR /app
COPY main.py install.py python.proto entrypoint.sh ./
RUN pip install grpcio  \
    && pip install grpcio-tools  \
    && pip install protobuf  \
    && pip install py4j  \
    && pip install RestrictedPython \
    && chmod +x entrypoint.sh
EXPOSE 50051
ENTRYPOINT ["./entrypoint.sh"]