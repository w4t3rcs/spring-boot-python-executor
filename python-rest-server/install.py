import os

imports = os.getenv("PYTHON_SERVER_IMPORTS", "")
delimiter = os.getenv("PYTHON_SERVER_IMPORTS_DELIMITER", ",")
split_imports = [imp.strip() for imp in imports.split(delimiter) if imp.strip()]

with open("requirements.txt", "w") as f:
    for import_declaration in split_imports:
        f.write(import_declaration + "\n")