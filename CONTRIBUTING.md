# Contributing to spring-boot-python-executor

Thank you for your interest in contributing to **spring-boot-python-executor**! Your contributions help improve the project and make it more useful to the community. This document provides guidelines to help you get started and ensure smooth collaboration.

---

## 1. Introduction

**spring-boot-python-executor** is a Spring Boot module that allows executing Python scripts directly from a Spring application. It supports:

- Execution of Python scripts with **SpEL** integration.
- Secure execution using **RestrictedPython**.
- REST and gRPC servers for isolated script execution.
- Starter and autoconfigure support for seamless Spring Boot integration.

Whether you want to report a bug, propose new features, or improve documentation, your contributions are welcome!

---

## 2. Code of Conduct

We expect all contributors to follow a respectful and professional attitude in discussions, issues, and pull requests. Please:

- Be respectful and considerate.
- Avoid offensive or discriminatory language.
- Focus on technical content and constructive feedback.

For a detailed guide, see the [Code of Conduct](CODE_OF_CONDUCT.md) (if available).

---

## 3. How to Contribute

### Reporting Issues

To report bugs or request new features:

1. Search existing issues to avoid duplicates.
2. Provide a clear and descriptive title.
3. Include relevant details:
   - Steps to reproduce the bug.
   - Expected vs. actual behavior.
   - Versions of Spring Boot, Python, and this module.
   - Logs or stack traces if applicable.
4. Use labels or milestones if you have permission.

GitHub Issues is the primary platform for tracking bugs and feature requests.

### Submitting Pull Requests

Follow these steps to submit a PR:

1. Fork the repository and clone it locally.
2. Create a new branch for your feature or fix:
```bash
git checkout -b feature/my-feature
```

3. Make your changes following the project’s coding standards.
4. Write clear and concise commit messages:
   * Include issue numbers if applicable, e.g., `Fixes #123`.
5. Push your branch and open a pull request against `main`.

**Branch naming conventions:**

* Features: `feature/short-description`
* Bug fixes: `fix/short-description`
* Documentation: `docs/short-description`

### Code Style and Quality

* Follow **Spring Boot and Java best practices**.
* Use consistent formatting and indentation.
* Linting tools: `Checkstyle`, `Spotless` or `IDE formatting rules`.
* Ensure your code passes all tests before submitting a PR.

### Testing

* The project uses **JUnit 5** for unit tests.
* Run tests locally with:

```bash
/mvnw test
```
* Write tests for any new functionality or bug fixes.
* Ensure all tests pass before submitting a PR.

---

## 4. Development Setup

To set up a development environment:

1. Clone the repository:

```bash
git clone https://github.com/w4t3rcs/spring-boot-python-executor.git
```
2. Navigate to the project directory:

 ```bash
 cd spring-boot-python-executor
 ```
3. Ensure you have Java 17+ and Maven installed.
4. Build the project:

```bash
./mvnw clean install
```
5. Optional: Run Docker containers for sandboxed execution if needed (see README).

---

## 5. Documentation

* Documentation is maintained in `README.md` and Javadoc comments.
* When adding new features, update the README with:

  * New endpoints, configuration options, or examples.
  * Any required setup instructions.
* Use clear Markdown formatting and consistent style.

---

## 6. Licensing

This project is licensed under the **MIT License**. By contributing, you agree that your contributions will be licensed under the same terms.
See the [LICENSE](LICENSE) file for full terms.

---

## 7. Acknowledgments

We appreciate all contributions from:

* Contributors who report issues or submit pull requests.
* Open-source libraries and frameworks used:
  * **Spring Boot**
  * **RestrictedPython**
  * **Py4J**
* The community for support and feedback.

---

Thank you for helping make **spring-boot-python-executor** better!
