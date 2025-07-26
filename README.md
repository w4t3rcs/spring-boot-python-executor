# Spring Boot Python Executor

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3%2B-green)
![License](https://img.shields.io/badge/License-MIT-blue)
![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen)

**A powerful bridge between Spring Boot and Python, enabling seamless execution of Python code from Java applications.**

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Modules](#-modules)
- [Features](#-features)
- [Requirements](#-requirements)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage Examples](#-usage-examples)
- [Example Module](#-example-module)
- [Advanced Usage](#-advanced-usage)
- [Python Server Setup](#-python-server-setup)
- [License](#-license)
- [Author](#-author)

## 🔍 Overview

Spring Boot Python Executor provides a flexible and powerful way to integrate Python functionality into your Spring Boot applications with minimal configuration. It allows you to execute Python code directly from your Java applications using various execution strategies, supporting both annotation-based and programmatic approaches, with Spring Expression Language (SpEL) integration for dynamic value resolution.

## 🏗️ Architecture

The library follows a modular architecture designed around several key principles:

1. **Separation of Concerns**: Each module has a specific responsibility in the Python execution pipeline
2. **Extensibility**: The system is designed to be extended with new execution strategies and resolvers
3. **Integration with Spring Boot**: Seamless integration with Spring Boot's autoconfiguration mechanism

### Core Components

- **Resolvers**: Transform Java values into Python code and vice versa or transform Python code to the necessary structure
- **Executors**: Execute Python code using different strategies (local, gRPC, REST)
- **Processors**: Orchestrate the execution flow and handle results
- **Annotations**: Provide declarative Python execution capabilities

## 📦 Modules

The project consists of the following modules: each with a specific responsibility:

### Core Module (`spring-boot-python-executor-core`)

The foundation of the library, providing the fundamental API and logic for executing and resolving Python scripts from Java in the Spring Boot ecosystem.

**Key Components:**
- `PythonProcessor`: Central component for processing Python scripts
- `PythonResolver`: Interface for resolving values between Java and Python
- `PythonExecutor`: Interface for executing Python code
- Properties classes
- Annotation definitions (`@PythonBefore`, `@PythonAfter`, `@PythonParam`)
- gRPC and Py4J integration classes

### Autoconfigure Module (`spring-boot-python-executor-autoconfigure`)

Provides Spring Boot autoconfiguration for the library, automatically configuring components based on application properties.

**Key Components:**
- `PythonAutoConfiguration`: Main autoconfiguration class
- Conditional configurations
- Default property values

### Starter Module (`spring-boot-python-executor-starter`)

Combines the core and autoconfigure modules to provide a seamless integration experience, following the standard Spring Boot starter pattern.

**Dependencies:**
- Core module
- Autoconfigure module

### Python REST Server (`python-rest-server`)

A FastAPI-based REST server implementation that executes Python code sent from the Java application, providing an alternative to the gRPC server.

**Key Components:**
- FastAPI REST endpoints
- Authentication middleware
- Request/response handling
- Error management


### Python gRPC Server (`python-grpc-server`)

A Python gRPC server implementation that executes Python code sent from the Java application.

**Key Components:**
- gRPC service implementation
- Authentication mechanism
- Secure execution environment
- Result serialization

## ✨ Features

- **Multiple Execution Strategies**: Support for both gRPC and REST-based Python execution
- **Annotation-based Execution**: Execute Python code before or after Java methods using annotations
- **Programmatic Execution**: Execute Python code programmatically using the `PythonProcessor`
- **SpEL Integration**: Use Spring Expression Language for dynamic value resolution in Python code
- **File-based Execution**: Load Python code from external files
- **Type Conversion**: Automatic conversion of Python results to Java types
- **Secure Execution**: Authentication and isolated execution environment
- **Flexible Configuration**: Extensive configuration options for all components

## 📋 Requirements

- Java 17 or higher
- Spring Boot 3.5.3 or higher
- Python 3.x (for the Python servers)

## 📥 Installation

### Maven

Add the starter dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.w4t3rcs</groupId>
    <artifactId>spring-boot-python-executor-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the starter dependency to your `build.gradle`:

```groovy
implementation 'io.github.w4t3rcs:spring-boot-python-executor-starter:1.0.0'
```

## ⚙️ Configuration

Configure the Python executor in your `application.yaml` or `application.properties`. Below is a comprehensive list of available properties with their default values and descriptions.

### File Properties

| Property                       | Default    | Description                                          |
|--------------------------------|------------|------------------------------------------------------|
| `spring.python.file.path`      | `/python/` | Base path for Python script files                    |
| `spring.python.file.cacheable` | `true`     | Whether to cache Python files for better performance |

### Resolver Properties

| Property                          | Default            | Description                              |
|-----------------------------------|--------------------|------------------------------------------|
| `spring.python.resolver.declared` | `spelython,result` | Comma-separated list of resolvers to use |

#### Spelython Resolver (SpEL Integration)

| Property                                                     | Default       | Description                                    |
|--------------------------------------------------------------|---------------|------------------------------------------------|
| `spring.python.resolver.spelython.regex`                     | `spel\\{.+?}` | Regex pattern to identify SpEL expressions     |
| `spring.python.resolver.spelython.spel.local-variable-index` | `#`           | Prefix for local variables in SpEL expressions |
| `spring.python.resolver.spelython.spel.position-from-start`  | `5`           | Position from start in the regex match         |
| `spring.python.resolver.spelython.spel.position-from-end`    | `1`           | Position from end in the regex match           |

#### Py4J Resolver

| Property                                  | Default                                     | Description                 |
|-------------------------------------------|---------------------------------------------|-----------------------------|
| `spring.python.resolver.py4j.import-line` | `from py4j.java_gateway import JavaGateway` | Import statement for Py4J   |
| `spring.python.resolver.py4j.gateway`     | `gateway = JavaGateway()`                   | Gateway initialization code |

#### Restricted Python Resolver

| Property                                                          | Default                                                                                      | Description                       |
|-------------------------------------------------------------------|----------------------------------------------------------------------------------------------|-----------------------------------|
| `spring.python.resolver.restricted-python.import-line`            | `from RestrictedPython import compile_restricted\nfrom RestrictedPython import safe_globals` | Import statements                 |
| `spring.python.resolver.restricted-python.code-variable-name`     | `source_code`                                                                                | Variable name for the source code |
| `spring.python.resolver.restricted-python.local-variables-name`   | `execution_result`                                                                           | Variable name for local variables |
| `spring.python.resolver.restricted-python.safe-result-appearance` | `r4java_restricted`                                                                          | Variable name for the result      |
| `spring.python.resolver.restricted-python.script-imports-regex`   | `(^import [\\w.]+$)`                                                                         | (^from [\\w.]+ import [\\w.]+$)   |(^from [\\w.]+ import [\\w.]+ as [\\w.]+$)` | Regex for allowed imports |
| `spring.python.resolver.restricted-python.print-enabled`          | `true`                                                                                       | Whether to allow print statements |

#### Result Resolver

| Property                                            | Default         | Description                                  |
|-----------------------------------------------------|-----------------|----------------------------------------------|
| `spring.python.resolver.result.regex`               | `o4java\\{.+?}` | Regex pattern to identify result expressions |
| `spring.python.resolver.result.appearance`          | `r4java`        | Variable name for the result                 |
| `spring.python.resolver.result.position-from-start` | `7`             | Position from start in the regex match       |
| `spring.python.resolver.result.position-from-end`   | `1`             | Position from end in the regex match         |

### Executor Properties

| Property                      | Default | Description                               |
|-------------------------------|---------|-------------------------------------------|
| `spring.python.executor.type` | `local` | Executor type: `local`, `rest`, or `grpc` |

#### Local Executor

| Property                                     | Default  | Description                      |
|----------------------------------------------|----------|----------------------------------|
| `spring.python.executor.local.start-command` | `python` | Command to start Python          |
| `spring.python.executor.local.loggable`      | `true`   | Whether to log execution details |

#### REST Executor

| Property                               | Default                                                                          | Description             |
|----------------------------------------|----------------------------------------------------------------------------------|-------------------------|
| `spring.python.executor.rest.host`     | `http://localhost`                                                               | REST server host        |
| `spring.python.executor.rest.port`     | `8000`                                                                           | REST server port        |
| `spring.python.executor.rest.uri`      | `${spring.python.executor.rest.host}:${spring.python.executor.rest.port}/script` | Full URI                |
| `spring.python.executor.rest.username` | (none)                                                                           | Authentication username |
| `spring.python.executor.rest.password` | (none)                                                                           | Authentication password |

#### gRPC Executor

| Property                               | Default                                                                   | Description             |
|----------------------------------------|---------------------------------------------------------------------------|-------------------------|
| `spring.python.executor.grpc.host`     | `localhost`                                                               | gRPC server host        |
| `spring.python.executor.grpc.port`     | `50051`                                                                   | gRPC server port        |
| `spring.python.executor.grpc.uri`      | `${spring.python.executor.grpc.host}:${spring.python.executor.grpc.port}` | Full URI                |
| `spring.python.executor.grpc.username` | (none)                                                                    | Authentication username |
| `spring.python.executor.grpc.password` | (none)                                                                    | Authentication password |

### Py4J Properties

| Property                             | Default     | Description                         |
|--------------------------------------|-------------|-------------------------------------|
| `spring.python.py4j.enabled`         | `false`     | Whether Py4J integration is enabled |
| `spring.python.py4j.host`            | `127.0.0.1` | Py4J server host                    |
| `spring.python.py4j.port`            | `25333`     | Py4J server port                    |
| `spring.python.py4j.python-port`     | `25334`     | Py4J Python port                    |
| `spring.python.py4j.connect-timeout` | `0`         | Connection timeout (0 = no timeout) |
| `spring.python.py4j.read-timeout`    | `0`         | Read timeout (0 = no timeout)       |
| `spring.python.py4j.loggable`        | `true`      | Whether to log Py4J operations      |

## 🚀 Usage Examples

### Annotation-based Execution

#### Execute Python code before a Java method

```java
@PythonBefore("print(spel{#name} + ' ' + spel{#surname})")
public void greet(String name, @PythonParam("surname") String surname) {
    System.out.println("Hello from Java: " + name + " " + surname);
}
```

#### Execute Python code after a Java method

```java
@PythonAfter("print(spel{#result} + ' ' + spel{#name})")
public String getMessage(String name) {
    return "Hello";
}
```

#### Execute Python code from a file

```java
@PythonBefore("path/to/script.py")
public void executeFromFile(String name) {
    // Method implementation
}
```

### Programmatic Execution

Execute Python code programmatically:

```java
@Service
@RequiredArgsConstructor
public class MyService {
    private final PythonProcessor pythonProcessor;
    
    public String executePython(String name, String surname) {
        String script = """
                result = spel{#name} + ' ' + spel{#surname}
                o4java{result}
                """;
        Map<String, Object> arguments = Map.of("name", name, "surname", surname);
        return pythonProcessor.process(script, String.class, arguments);
    }
}
```

## 📝 Example Module

The `example` module provides a complete reference implementation of the library. If you want to see how to use this library in a real application, check this module first.

### Key Components in the Example Module

- `ExamplePythonService`: Demonstrates annotation-based execution
- `application.yaml`: Shows configuration options
- Python script files: Examples of external Python scripts

### Advanced Example: Data Processing Service

```java
@Service
@RequiredArgsConstructor
public class DataProcessingService {
    private final PythonProcessor pythonProcessor;
    
    @PythonBefore("""
                  import pandas as pd
                  import numpy as np
                  data = pd.DataFrame(spel{#rawData})
                  processed_data = data.groupby('category').agg({'value': ['mean', 'sum']})
                  o4java{processed_data.to_dict()}
                  """)
    public void processData(List<Map<String, Object>> rawData) {
        // The Python code will execute before this method
        System.out.println("Data processing completed");
    }
    
    public Map<String, Object> analyzeTimeSeriesData(List<Double> timeSeriesData, String analysisType) {
        String script = """
                import numpy as np
                from statsmodels.tsa.arima.model import ARIMA
                
                data = np.array(spel{#timeSeriesData})
                analysis_type = spel{#analysisType}
                
                result = {}
                
                if analysis_type == 'basic':
                    result['mean'] = np.mean(data)
                    result['std'] = np.std(data)
                    result['min'] = np.min(data)
                    result['max'] = np.max(data)
                elif analysis_type == 'forecast':
                    model = ARIMA(data, order=(1, 1, 1))
                    model_fit = model.fit()
                    forecast = model_fit.forecast(steps=5)
                    result['forecast'] = forecast.tolist()
                
                o4java{result}
                """;
        
        Map<String, Object> arguments = Map.of(
            "timeSeriesData", timeSeriesData,
            "analysisType", analysisType
        );
        
        return (Map<String, Object>) pythonProcessor.process(script, Map.class, arguments);
    }
}
```

## 🔧 Advanced Usage

### SpEL Integration

Use SpEL expressions in your Python code with the `spel{#expression}` syntax. The following examples demonstrate the syntax:

> **Note:** The examples below use special syntax processed by the library, not standard Python syntax.

```
# Access method parameters
name = spel{#name}

# Access Spring beans
currentUser = spel{@userService.getCurrentUser()}

# Use SpEL operators
isAdmin = spel{#user.roles.?[name == 'ADMIN'].size() > 0}

# Access nested properties
address = spel{#customer.address.street}

# Use conditional expressions
greeting = spel{#time < 12 ? 'Good morning' : 'Good afternoon'}
```

### Result Handling

Return values from Python to Java using the `o4java{variable}` syntax:

> **Note:** The examples below use special syntax processed by the library, not standard Python syntax.

```
# Simple string result
result = "Hello, " + spel{#name}
o4java{result}

# Dictionary result (converts to Java Map)
user_data = {
    "name": spel{#user.name},
    "email": spel{#user.email},
    "roles": spel{#user.roles}
}
o4java{user_data}

# List result (converts to Java List)
numbers = [1, 2, 3, 4, 5]
squared = [n * n for n in numbers]
o4java{squared}

# Complex nested structures
response = {
    "user": {
        "id": spel{#user.id},
        "name": spel{#user.name}
    },
    "permissions": spel{#user.permissions},
    "metadata": {
        "lastLogin": spel{#user.lastLogin},
        "active": spel{#user.active}
    }
}
o4java{response}
```

### Working with Files

You can store Python scripts in files and reference them in annotations:

```java
@PythonBefore("scripts/data_processing.py")
public void processData(List<Map<String, Object>> data) {
    // Implementation
}
```

The Python file (`data_processing.py`) can use the same SpEL and result syntax:

> **Note:** The example below uses special syntax processed by the library, not standard Python syntax.

```
import pandas as pd
import numpy as np

# Access method parameters using SpEL
raw_data = spel{#data}

# Process the data
df = pd.DataFrame(raw_data)
result = {
    "summary": df.describe().to_dict(),
    "correlations": df.corr().to_dict()
}

# Return the result to Java
o4java{result}
```

## 🖥️ Python Server Setup

The project includes two Python server implementations: a gRPC server and a REST server. Both servers are designed to be run in Docker containers, but can also be run directly on the host machine.

### Docker Setup (Recommended)

#### gRPC Server

1. Run the container:
   ```
   docker run -d --name python-grpc-server \
     -p 50051:50051 \
     -e PYTHON_SERVER_USERNAME=your-username \
     -e PYTHON_SERVER_PASSWORD=your-password \
     python-executor-grpc
   ```

#### REST Server

1. Build the Docker image:
   ```
   docker run -d -e PYTHON_SERVER_USERNAME=root -e PYTHON_SERVER_PASSWORD=password -p 8000:8000 w4t3rcs/spring-boot-python-executor-python-rest-server
   ```

2. Run the container:
   ```
   docker run -d -e PYTHON_SERVER_USERNAME=root -e PYTHON_SERVER_PASSWORD=password -p 50051:50051 w4t3rcs/spring-boot-python-executor-python-grpc-server
   ```

### Environment Variables

Both servers use the following environment variables:

| Variable                                | Description                                           | Default    |
|-----------------------------------------|-------------------------------------------------------|------------|
| `PYTHON_SERVER_USERNAME`                | Username for authentication                           | (required) |
| `PYTHON_SERVER_PASSWORD`                | Password for authentication                           | (required) |
| `PYTHON_SERVER_HOST`                    | Host to bind the server to                            | `0.0.0.0`  |
| `PYTHON_SERVER_PORT`                    | Port to bind the server to                            | `50051`    |
| `PYTHON_RESULT_APPEARANCE`              | Variable name for the result in the execution context | `r4java`   |
| `PYTHON_ADDITIONAL_IMPORTS`             | Additional Python packages to install                 | `""`       |
| `PYTHON_ADDITIONAL_IMPORTS_DELIMITER`   | Delimiter for additional imports                      | `,`        |
| `PYTHON_SERVER_THREAD_POOL_MAX_WORKERS` | Maximum number of worker threads (only for gRPC)      | `10`       |

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

- [w4t3rcs](https://github.com/w4t3rcs) - w4t3rofficial@gmail.com