# 🐍 Spring Boot Python Executor

<img src="logo.svg" alt="logo :D">

[![Maven Central](https://img.shields.io/maven-central/v/io.github.w4t3rcs/spring-boot-python-executor-starter.svg)](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor-starter)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.w4t3rcs/spring-boot-python-executor-cache-starter.svg)](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor-cache-starter)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.w4t3rcs/spring-boot-python-executor-testcontainers.svg)](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor-testcontainers)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.w4t3rcs/spring-boot-python-executor-dependencies.svg)](https://central.sonatype.com/artifact/io.github.w4t3rcs/spring-boot-python-executor-dependencies)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java: 17+](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot: 3.5.3+](https://img.shields.io/badge/Spring%20Boot-3.5.3%2B-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Python: 3.10+](https://img.shields.io/badge/Python-3.10%2B-blue.svg)](https://www.python.org/downloads/)
![Tests Passed: 99%](https://img.shields.io/badge/Tests%20Passed-99%25-green.svg)

<hr>

## 📋 Table of Contents

- [TL;DR](#-tldr)
- [Introduction](#-introduction)
- [Features & Architecture](#-features--architecture)
  - [Core Components](#core-components)
  - [Architecture Diagram](#architecture-diagram)
  - [Security](#security)
  - [Cache](#cache)
  - [Testing](#testing)
- [Installation and Setup](#-installation-and-setup)
- [Configuration](#-configuration)
  - [File Properties](#file-properties)
  - [Executor Properties](#executor-properties)
  - [Resolver Properties](#resolver-properties)
  - [Py4J Properties](#py4j-properties)
  - [Cache Properties](#cache-properties)
  - [Aspect Properties](#aspect-properties)
- [Execution Modes](#-execution-modes)
  - [Local Execution](#local-execution)
  - [REST Execution](#rest-execution)
  - [gRPC Execution](#grpc-execution)
- [Usage Examples](#-usage-examples)
  - [Simple Example: Basic Calculation](#basic-examples)
  - [Realistic Example: Integration with Business Logic](#advanced-examples)
- [Python Server](#-python-server)
  - [REST Server](#rest-server)
  - [gRPC Server](#grpc-server)
  - [Environment Variables](#environment-variables)
- [Requirements](#-requirements)
- [License](#-license)
- [Contributing and Feedback](#-contributing-and-feedback)
  - [Reporting Issues](#reporting-issues)
  - [Feature Requests](#feature-requests)

<hr>

## 🚀 TL;DR

- **Execute Python scripts** securely from your Spring Boot applications
- Use **annotations** or direct API calls with **SpEL integration**
- Supports **local execution**, **REST API**, and **gRPC** communication modes
- Built-in security with **RestrictedPython**
- [Jump to Quick Start](#-installation-and-setup)

## 📝 Introduction

**Spring Boot Python Executor** is a library that enables secure and extensible execution of Python scripts from Java applications built with Spring Boot. It bridges the gap between Java and Python ecosystems, allowing developers to leverage Python's strengths while maintaining the robustness of Spring Boot applications.

This library is designed for Spring Boot developers who need to integrate Python functionality (like data processing, machine learning algorithms, or specialized libraries) into their Java applications without the complexity of manual process management.

## 🏗️ Features & Architecture

Spring Boot Python Executor provides a flexible architecture for executing Python code from Java:

### Core Components

- **PythonExecutor**: Interface for executing Python scripts with three implementations:
  - Local execution (in-process)
  - REST-based execution (separate container)
  - gRPC-based execution (separate container)
- **PythonResolver**: Processes scripts before execution, supporting:
  - SpEL integration for accessing Java variables
  - RestrictedPython for secure execution
  - Result resolution for capturing Python output
- **PythonProcessor**: Connects resolvers and executors

### Architecture Diagram

If you are willing to use AOP based script processing, 
your Python code will move from Python annotation to a configured PythonProcessor implementation object

```mermaid
---
title: Python annotations flow
---

classDiagram
    PythonBefores --> PythonAnnotationEvaluator: An annotation container that holds multiple @PythonBefore annotations is passed to the evaluator
    PythonBefore --> PythonAnnotationEvaluator: A single @PythonBefore annotation is passed to the evaluator with script and profiles info
    PythonAfters --> PythonAnnotationEvaluator: An annotation container that holds multiple @PythonAfter annotations is passed to the evaluator
    PythonAfter --> PythonAnnotationEvaluator: A single @PythonAfter annotation is passed to the evaluator with script and profiles info
    PythonAnnotationEvaluator --> PythonAnnotationValueCompounder: The evaluator requests the compounder to merge annotation values into a unified structure
    PythonAnnotationEvaluator <-- PythonAnnotationValueCompounder: Returns a merged map of Python code and active profiles
    PythonAnnotationEvaluator --> ProfileChecker: The evaluator checks if the annotation’s activeProfiles match the current application profiles
    PythonAnnotationEvaluator <-- ProfileChecker: Executes a callback if profiles match
    PythonAnnotationEvaluator --> PythonArgumentsExtractor: Extracts method arguments from the JoinPoint
    PythonAnnotationEvaluator <-- PythonArgumentsExtractor: Returns a map of argument names to their values
    PythonAnnotationEvaluator --> PythonProcessor: Finally, executes the Python script using the provided arguments
    PythonAnnotationValueCompounder --> PythonAnnotationValueExtractor: The compounder delegates to the several extractors to get raw values from annotations
    PythonAnnotationValueCompounder <-- PythonAnnotationValueExtractor: Returns the raw annotation values
    PythonAnnotationValueExtractor --> PythonMethodExtractor: The extractor gets method metadata from the JoinPoint
    PythonAnnotationValueExtractor <-- PythonMethodExtractor: Returns the method object
    PythonArgumentsExtractor --> PythonMethodExtractor: The arguments extractor retrieves method parameters from the JoinPoint
    PythonArgumentsExtractor <-- PythonMethodExtractor: Returns a map of parameter names to their values
    
    class PythonBefores {
        +PythonBefore[] value()
    }
    
    class PythonBefore {
        +String value()
        +String script()
        +String[] activeProfiles()
    }
    
    class PythonAfters {
        +PythonAfter[] value()
    }
    
    class PythonAfter {
        +String value()
        +String script()
        +String[] activeProfiles()
    }
    
    class PythonAnnotationEvaluator {
        <<interface>> 
        +<A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass);
        +<A extends Annotation> void evaluate(JoinPoint joinPoint, Class<? extends A> annotationClass, Map<String, Object> additionalArguments);
    }
    
    class PythonAnnotationValueCompounder {
        <<interface>>
        +<A extends Annotation> Map<String, String[]> compound(JoinPoint joinPoint, Class<? extends A> annotationClass);
    }
    
    class ProfileChecker { 
        <<interface>>
        +void doOnProfiles(String[] profiles, Runnable action);
    }
    
    class PythonArgumentsExtractor {
        <<interface>>
        +Map<String, Object> getArguments(JoinPoint joinPoint)
        +Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments)
    }
    
    class PythonProcessor {
        <<interface>>
        +void process(String script)
        +void process(String script, Map<String, Object> arguments)
        +R process(String script, Class<? extends R> resultClass)
        +R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments)
    }
    
    class PythonAnnotationValueExtractor {
        <<interface>>
        +<A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass);
    }
    
    class PythonMethodExtractor {
        <<interface>>
        +Method getMethod(JoinPoint joinPoint);
        +Map<String, Object> getMethodParameters(JoinPoint joinPoint);
    }
```

Also, you can use only PythonProcessor object to execute Python code:
```mermaid
---
title: PythonProcessor flow
---

classDiagram
    PythonProcessor --> PythonFileHandler: Firstly, the script is checked whether it's a .py file or String script
    PythonProcessor <-- PythonFileHandler: If it's a file, the content is read as a String value and returned
    PythonProcessor --> PythonResolverHolder: Secondly, the script may be  transformed by multiple PythonResolver implementations. You can configure declared implementations by using spring.python.resolver.declared property
    PythonProcessor <-- PythonResolverHolder: Returns resolved script from multiple PythonResolvers
    PythonProcessor --> PythonExecutor: Finally, the script is executed by PythonExecutor implementation. You can choose needed implementation by configuring the property named spring.python.executor.type
    PythonProcessor <-- PythonExecutor: The method returns null, because of annotation usage, but if you would like to get specific result object you need to call process(...) function manually
    
    class PythonProcessor {
        <<interface>>
        +void process(String script)
        +void process(String script, Map<String, Object> arguments)
        +R process(String script, Class<? extends R> resultClass)
        +R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments)
    }
    
    class PythonFileHandler {
        <<interface>>
        +boolean isPythonFile(String path)
        +void writeScriptBodyToFile(String path, String script)
        +void writeScriptBodyToFile(Path path, String script)
        +String readScriptBodyFromFile(String path)
        +String readScriptBodyFromFile(Path path)
        +String readScriptBodyFromFile(String path, UnaryOperator<String> mapper)
        +String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper)
        +Path getScriptPath(String path)
    }
    
    class PythonResolverHolder {
        <<interface>>
        +Iterator<PythonResolver> iterator()
        +void forEach(Consumer<? super PythonResolver> action)
        +Spliterator<PythonResolver> spliterator()
        +Stream<PythonResolver> stream()
        +String resolveAll(String script, Map<String, Object> arguments);
        +List<PythonResolver> getResolvers();
    }
    
    class PythonExecutor {
        <<interface>>
        +R execute(String script, Class<? extends R> resultClass)
    }
```

### Security

The library uses RestrictedPython to create a sandboxed environment for Python execution, preventing potentially harmful operations while allowing controlled script execution.
Also, you can acknowledge the security policy of the project [here](https://github.com/w4t3rcs/spring-boot-python-executor/blob/master/SECURITY.md).

### Cache

The library supports transparent result caching via the Spring Cache abstraction.
It allows storing the results of Python script executions to avoid repeated computation.
Any Spring-compatible CacheManager can be used, including in-memory, Redis, Caffeine, etc.

### Testing

The library allows testing Python servers using its own Testcontainers GenericContainer implementation.

## 📦 Installation and Setup

### Maven

To set up a proper version of the project add this to your `pom.xml`:

```xml
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.github.w4t3rcs</groupId>
                <artifactId>spring-boot-python-executor-dependencies</artifactId>
                <version>1.0.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

Add the starter dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.w4t3rcs</groupId>
    <artifactId>spring-boot-python-executor-starter</artifactId>
</dependency>
```

If you want to have caching abilities, also add this:

```xml
<dependency>
    <groupId>io.github.w4t3rcs</groupId>
    <artifactId>spring-boot-python-executor-cache-starter</artifactId>
</dependency>
```

If you want to test the work of PythonProcessor instance, add this:

```xml
<dependency>
    <groupId>io.github.w4t3rcs</groupId>
    <artifactId>spring-boot-python-executor-testcontainers</artifactId>
</dependency>
```

### Gradle

To set up a proper version of the project add this to your `build.gradle`:
```groovy
implementation platform('io.github.w4t3rcs:spring-boot-python-executor-dependencies:1.0.0')
```

Add the starter dependency to your `build.gradle`:

```groovy
implementation 'io.github.w4t3rcs:spring-boot-python-executor-starter'
```

If you want to have caching abilities, also add this:

```groovy
implementation 'io.github.w4t3rcs:spring-boot-python-executor-cache-starter'
```

If you want to test the work of PythonProcessor instance, add this:

```groovy
implementation 'io.github.w4t3rcs:spring-boot-python-executor-testcontainers'
```

## ⚙️ Configuration

### File Properties

| Property                       | Description                          | Default    |
|--------------------------------|--------------------------------------|------------|
| `spring.python.file.path`      | Base path for Python script files    | `/python/` |

### Executor Properties

| Property                      | Description                          | Default |
|-------------------------------|--------------------------------------|---------|
| `spring.python.executor.type` | Execution mode: local, rest, or grpc | `local` |


#### Local Executor Properties

| Property                                     | Description                  | Default  |
|----------------------------------------------|------------------------------|----------|
| `spring.python.executor.local.start-command` | Command to start Python      | `python` |
| `spring.python.executor.local.loggable`      | Whether to log Python output | `true`   |

#### REST Executor Properties

| Property                               | Description               | Default                                                                          |
|----------------------------------------|---------------------------|----------------------------------------------------------------------------------|
| `spring.python.executor.rest.host`     | REST server host          | `http://localhost`                                                               |
| `spring.python.executor.rest.port`     | REST server port          | `8000`                                                                           |
| `spring.python.executor.rest.token`    | Authentication token      | `-` (required)                                                                   |
| `spring.python.executor.rest.uri`      | Full URI to REST endpoint | `${spring.python.executor.rest.host}:${spring.python.executor.rest.port}/script` |

#### gRPC Executor Properties

| Property                            | Description              | Default                                                                   |
|-------------------------------------|--------------------------|---------------------------------------------------------------------------|
| `spring.python.executor.grpc.host`  | gRPC server host         | `localhost`                                                               |
| `spring.python.executor.grpc.port`  | gRPC server port         | `50051`                                                                   |
| `spring.python.executor.grpc.token` | Authentication token     | `-` (required)                                                            |
| `spring.python.executor.grpc.uri`   | Full URI to gRPC service | `${spring.python.executor.grpc.host}:${spring.python.executor.grpc.port}` |

### Resolver Properties

#### Core Resolver Properties

| Property                                      | Description                                                                             | Default                                                                                |
|-----------------------------------------------|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| `spring.python.resolver.declared`             | Enabled resolvers: `result`, `spelython`, `py4j`, `restricted_python`, `printed_result` | `spelython, result, printed_result`                                                    |
| `spring.python.resolver.script-imports-regex` | Regular expression to match import statements                                           | `(^import [\\w.]+$)\|(^import [\\w.]+ as [\\w.]+$)\|(^from [\\w.]+ import [\\w., ]+$)` |

#### Result Resolver Properties

| Property                                            | Description                                    | Default         |
|-----------------------------------------------------|------------------------------------------------|-----------------|
| `spring.python.resolver.result.regex`               | Regular expression to match result expressions | `o4java\\{.+?}` |
| `spring.python.resolver.result.appearance`          | Variable name for results                      | `r4java`        |
| `spring.python.resolver.result.position-from-start` | Position from start of match for extraction    | `7`             |
| `spring.python.resolver.result.position-from-end`   | Position from end of match for extraction      | `1`             |

#### Spelython Resolver Properties

| Property                                                     | Description                                  | Default       |
|--------------------------------------------------------------|----------------------------------------------|---------------|
| `spring.python.resolver.spelython.regex`                     | Regular expression to match SpEL expressions | `spel\\{.+?}` |
| `spring.python.resolver.spelython.spel.local-variable-index` | Prefix for local variables in SpEL           | `#`           |
| `spring.python.resolver.spelython.spel.position-from-start`  | Position from start of match for extraction  | `5`           |
| `spring.python.resolver.spelython.spel.position-from-end`    | Position from end of match for extraction    | `1`           |

#### Py4J Resolver Properties

| Property                                         | Description                            | Default                                                                                                                                       |
|--------------------------------------------------|----------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| `spring.python.resolver.py4j.import-line`        | Import statement for Py4J              | `from py4j.java_gateway import JavaGateway, GatewayParameters`                                                                                |
| `spring.python.resolver.py4j.gateway-object`     | Gateway object initialization template | `gateway = JavaGateway(\n\tgateway_parameters=GatewayParameters(\n\t\t%s\n\t)\n)`                                                             |
| `spring.python.resolver.py4j.gateway-properties` | Gateway properties for initialization  | `address=\"${spring.python.py4j.host}\", port=${spring.python.py4j.port}, auth_token=\"${spring.python.py4j.auth-token}\", auto_convert=True` |

#### Restricted Python Resolver Properties

| Property                                                          | Description                            | Default                                                                                      |
|-------------------------------------------------------------------|----------------------------------------|----------------------------------------------------------------------------------------------|
| `spring.python.resolver.restricted-python.import-line`            | Import statements for RestrictedPython | `from RestrictedPython import compile_restricted\nfrom RestrictedPython import safe_globals` |
| `spring.python.resolver.restricted-python.code-variable-name`     | Variable name for source code          | `source_code`                                                                                |
| `spring.python.resolver.restricted-python.local-variables-name`   | Variable name for local variables      | `execution_result`                                                                           |
| `spring.python.resolver.restricted-python.safe-result-appearance` | Variable name for safe results         | `r4java_restricted`                                                                          |
| `spring.python.resolver.restricted-python.print-enabled`          | Whether to enable print functionality  | `true`                                                                                       |

### Py4J Properties

| Property                             | Description                            | Default        |
|--------------------------------------|----------------------------------------|----------------|
| `spring.python.py4j.enabled`         | Whether to enable Py4J                 | `false`        |
| `spring.python.py4j.host`            | Py4J server host                       | `localhost`    |
| `spring.python.py4j.port`            | Py4J server port                       | `25333`        |
| `spring.python.py4j.python-host`     | Py4J Python host                       | `localhost`    |
| `spring.python.py4j.python-port`     | Py4J Python port                       | `25334`        |
| `spring.python.py4j.auth-token`      | Authentication token for Py4J          | `-` (required) |
| `spring.python.py4j.connect-timeout` | Connection timeout in milliseconds (0) | `0`            |
| `spring.python.py4j.read-timeout`    | Read timeout in milliseconds (0)       | `0`            |
| `spring.python.py4j.loggable`        | Whether to log Py4J operations         | `true`         |

### Cache Properties

| Property                                 | Description                                                                                                                                      | Default                |
|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| `spring.python.cache.enabled`            | Whether to enable autoconfiguration for caching or not (notice that you must have @EnableCaching and CacheManager bean provider in your project) | `true`                 |
| `spring.python.cache.levels`             | Enabled Python script flow phases where the needed Caching... bean should be created: `file`, `resolver`, `executor`, `processor`                | `file, processor`      |
| `spring.python.cache.name.file-bodies`   | Default name for Cache object that contains script bodies that have been read from file                                                          | `fileBodiesCache`      |
| `spring.python.cache.name.file-paths`    | Default name for Cache object that contains script paths that have been read from file                                                           | `filePathsCache`       |
| `spring.python.cache.name.resolver`      | Default name for Cache object that contains resolved scripts                                                                                     | `pythonResolverCache`  |
| `spring.python.cache.name.executor`      | Default name for Cache object that contains executed results                                                                                     | `pythonExecutorCache`  |
| `spring.python.cache.name.processor`     | Default name for Cache object that contains processed scripts (executed resolved scripts)                                                        | `pythonProcessorCache` |
| `spring.python.cache.key.hash-algorithm` | Key body hash algorithm                                                                                                                          | `SHA-256`              |
| `spring.python.cache.key.charset`        | Key body charset                                                                                                                                 | `UTF-8`                |
| `spring.python.cache.key.delimiter`      | Key delimiter between key prefix, key body and key suffix                                                                                        | `_`                    |

Note that if you want to specify your own cache instances using `spring.cache.cache-names`,
you must also add names from `spring.python.cache.names` or it will fail with `NullPointerException`

### Aspect Properties

| Property                                        | Description                                                                              | Default        |
|-------------------------------------------------|------------------------------------------------------------------------------------------|----------------|
| `spring.python.aspect.async.scopes`             | What annotations should process scripts asynchronously (`before`, `after`)               | `before,after` |
| `spring.python.aspect.async.core-pool-size`     | Core thread pool size for async Python execution                                         | `10`           |
| `spring.python.aspect.async.max-pool-size`      | Maximum thread pool size for async Python execution                                      | `50`           |
| `spring.python.aspect.async.queue-capacity`     | Queue capacity for pending async Python tasks                                            | `100`          |
| `spring.python.aspect.async.thread-name-prefix` | Prefix for async Python executor thread names                                            | `AsyncPython-` |
| `spring.python.aspect.async.rejection-policy`   | Policy for handling rejected tasks (`caller_runs`, `abort`, `discard`, 'discard_oldest') | `caller_runs`  |

## 🔄 Execution Modes

### Local Execution

Execute Python scripts in a local process on the same machine as your Java application. 
This implementation has a bunch of limitations, so it's a lot more suggestible to use REST/gRPC implementations.
To get the result from a script, you must have declared `ResultResolver` and `PrintedResultResolver`
(REST/gRPC only needs `ResultResolver`) in your application.properties:

```properties
spring.python.resolver.declared=result, printed_result
```

### REST Execution

Execute Python scripts via REST API in a separate container.

### gRPC Execution

Execute Python scripts via gRPC in a separate container for better performance.

## 🐍 Python Server

### REST Server

The REST server provides an HTTP endpoint for executing Python scripts.

#### Running with Docker

```bash
docker run -p 8000:8000 \
  -e PYTHON_SERVER_TOKEN=<your-security-token> \
  w4t3rcs/spring-boot-python-executor-python-rest-server
```

#### Testing with curl

```bash
curl -X POST http://localhost:8000/script \
  -H "Content-Type: application/json" \
  -H "X-Token: <your-security-token>" \
  -d '{\"script": \"r4java = 2 + 2\"}'
```

### gRPC Server

The gRPC server provides a high-performance interface for executing Python scripts.

#### Running with Docker

```bash
docker run -p 50051:50051 \
  -e PYTHON_SERVER_TOKEN=<your-security-token> \
  w4t3rcs/spring-boot-python-executor-python-grpc-server
```

#### Testing with grpcurl

```bash
grpcurl -plaintext -d '{\"script": \"r4java = 2 + 2\"}' \
  -H 'x-token: <your-security-token>' \
  localhost:50051 PythonService/SendCode
```

### Environment Variables

| Variable                                | Description                    | Default                    | Server    |
|-----------------------------------------|--------------------------------|----------------------------|-----------|
| `PYTHON_SERVER_TOKEN`                   | Authentication token           | -                          | Both      |
| `PYTHON_SERVER_HOST`                    | Server bind address            | 0.0.0.0                    | Both      |
| `PYTHON_SERVER_PORT`                    | Server port                    | 8000 (REST) / 50051 (gRPC) | Both      |
| `PYTHON_SERVER_THREAD_POOL_MAX_WORKERS` | Max worker threads             | 10                         | gRPC only |
| `PYTHON_RESULT_APPEARANCE`              | Result variable name           | r4java                     | Both      |
| `PYTHON_ADDITIONAL_IMPORTS`             | Additional Python packages     | -                          | Both      |
| `PYTHON_ADDITIONAL_IMPORTS_DELIMITER`   | Delimiter for imports          | ,                          | Both      |
| `PYTHON_LOGGING_ENABLED`                | Whether to enable request logs | True                       | Both      |

#### PYTHON_ADDITIONAL_IMPORTS

This environment variable allows you to specify additional Python packages to install in the server container. For example, setting `PYTHON_ADDITIONAL_IMPORTS=numpy,pandas,scikit-learn` will install these packages when the container starts, making them available for your Python scripts.

## 💻 Usage Examples

If you want to check out how the library works,
you can check [demo-app](https://github.com/w4t3rcs/spring-boot-python-executor/tree/master/demo-app) module or examples below

### Basic Examples

#### Using Annotations

Spring Boot Python Executor provides annotations for executing Python code before or after a Java method:

```java
@Service
@RequiredArgsConstructor
public class ExampleService {
    // Execute Python code before the Java method
    @PythonBefore("print(spel{#name} + ' ' + spel{#surname})")
    public void executeBefore(String name, @PythonParam("surname") String lastName) {
        System.out.println("Hello from Java after Python: " + name + " : " + lastName);
    }
    
    // Execute Python code from a file
    @PythonBefore("example.py")
    public void executeFromFile(String name) {
        System.out.println("Hello from Java after Python file: " + name);
    }
    
    // Execute Python code after the Java method
    // The method's return value is available as #result
    @PythonAfter("print(spel{#result} + ' ' + spel{#name} + ' ' + spel{#sur})")
    public String executeAfter(String name, @PythonParam("sur") String surname) {
        System.out.println("Hello from Java before Python: " + name + " : " + surname);
        return "Python app is greeting you:";
    }
}
```

#### Direct Use of PythonProcessor

For more control, you can directly use the `PythonProcessor`:

```java
@Service
@RequiredArgsConstructor
public class CalculationService {
    private final PythonProcessor pythonProcessor;
    
    public Integer add(int a, int b) {
        String script = """
                # Simple addition in Python
                result = spel{#a} + spel{#b}
                o4java{result}  # This value will be returned to Java
                """;
        Map<String, Object> arguments = Map.of("a", a, "b", b);
        PythonExecutionResponse<Integer> response = pythonProcessor.process(script, Integer.class, arguments);
        return response.body();
    }
}
```

### Advanced Examples

#### Integration with Business Logic

```java
@Service
@RequiredArgsConstructor
public class PricingService {
    private final PythonProcessor pythonProcessor;
    
    public double calculatePrice(Product product, Customer customer) {
        String script = """
                # Complex pricing algorithm in Python
                base_price = spel{#product.basePrice}
                discount = 0
                
                # Apply customer loyalty discount
                if spel{#customer.loyaltyYears} > 2:
                    discount += 0.05
                
                # Apply volume discount
                if spel{#product.quantity} > 10:
                    discount += 0.03
                
                final_price = base_price * (1 - discount)
                o4java{final_price}  # Return the calculated price to Java
                """;
        
        Map<String, Object> arguments = Map.of(
            "product", product,
            "customer", customer
        );
        
        PythonExecutionResponse<Double> response = pythonProcessor.process(script, Double.class, arguments);
        return response.body();
    }
}
```

#### Machine Learning Integration

This example demonstrates how to use Python's machine learning libraries from Java.
Note that if you use `python-rest-server` or `python-grpc-server` you'll need to set the `PYTHON_ADDITIONAL_IMPORTS` environment variable when running the Python server to make these libraries available.

```java
@Service
@RequiredArgsConstructor
public class SentimentAnalysisService {
    private final PythonProcessor pythonProcessor;
    
    public double analyzeSentiment(String text) {
        String script = """
                from sklearn.feature_extraction.text import CountVectorizer
                import numpy as np
                
                # Simple sentiment analysis using predefined positive and negative words
                positive_words = ['good', 'great', 'excellent', 'amazing', 'wonderful', 'best', 'love']
                negative_words = ['bad', 'terrible', 'awful', 'worst', 'hate', 'poor', 'disappointing']
                
                # Get the input text from Java
                text = spel{#text}.lower()
                
                # Count positive and negative words
                positive_count = sum(1 for word in positive_words if word in text)
                negative_count = sum(1 for word in negative_words if word in text)
                
                # Calculate sentiment score (-1 to 1)
                total = positive_count + negative_count
                if total == 0:
                    sentiment = 0
                else:
                    sentiment = (positive_count - negative_count) / total
                
                # Return the sentiment score to Java
                o4java{sentiment}
                """;
        
        Map<String, Object> arguments = Map.of("text", text);

        PythonExecutionResponse<Double> response = pythonProcessor.process(script, Double.class, arguments);
        return response.body();
    }
}
```

To run this example with the Python server, you would need to include the required libraries:

```bash
docker run -p 8000:8000 \
  -e PYTHON_SERVER_USERNAME=user \
  -e PYTHON_SERVER_PASSWORD=pass \
  -e PYTHON_ADDITIONAL_IMPORTS=scikit-learn,numpy,scipy \
  w4t3rcs/spring-boot-python-executor-python-rest-server
```

## 📋 Requirements

- Java 17+
- Spring Boot 3.5.3+
- Python 3.10+ (for local execution or server containers)
- Docker (for REST/gRPC backend)

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 🤝 Contributing and Feedback

### Reporting Issues

If you encounter any issues or have suggestions for improvements, please create an issue in the GitHub repository.

### Feature Requests

For feature requests, please create an issue with a detailed description of the proposed feature and its use cases.