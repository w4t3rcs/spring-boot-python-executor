import os

base_imports = ["fastapi", "uvicorn", "py4j", "RestrictedPython"]
imports = os.getenv("PYTHON_SERVER_IMPORTS", "")
delimiter = os.getenv("PYTHON_SERVER_IMPORTS_DELIMITER", ",")
split_imports = base_imports + [imp.strip() for imp in imports.split(delimiter) if imp.strip()]

with open("requirements.txt", "w") as f:
    for import_declaration in split_imports:
        f.write(import_declaration + "\n")