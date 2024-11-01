
# Polyglot Python-Java Application on GraalVM

![GraalPy](https://img.shields.io/badge/GraalPy-24.1.1-007d9c)
![Java](https://img.shields.io/badge/Java-23--graal-brightgreen)
![Python](https://img.shields.io/badge/Python-3.11-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

This simple project is meant as a jumping off point for a polyglot Python-Java application on GraalVM.

## Table of Contents
- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Introduction
This project demonstrates how to create a polyglot application using GraalVM, integrating both Java and Python code. It serves as a starting point for developers interested in leveraging GraalVM's polyglot capabilities.

## Installation
To get started with this project, you need to have GraalVM installed. Follow the instructions on the [GraalVM website](https://www.graalvm.org/) to install GraalVM for your operating system.

### Prerequisites
- GraalPy 24.1.1
- Java 23.0.1-graal
- Python 3.11

### Steps
1. Clone the repository:
    ```sh
    git clone https://github.com/ih0r-d/graalvm-polyglot-sandbox.git
    ```
2. Build a project with install the necessary Python dependencies via maven plugin:
    ```sh
    ./mvnw clean package -T4C
    ```

## Usage
To run the polyglot application, use the following command:
   ```sh
   java -jar target/graalpy-demo.jar
   ```

## Project Structure
```
├── LICENSE
├── README.md
├── build.sh
├── error.log
├── mvnw
├── mvnw.cmd
├── pom.xml
├── python-resources
│         ├── home
│         │         ├── lib-graalpython
│         │         └── lib-python
│         │             └── 3
│         └── venv
│             ├── bin
│             ├── include
│             │         └── python3.11
│             └── lib
│                 └── python3.11
└── src
    └── main
        ├── java
        │    └── com
        │        └── github
        │                 └── ih0rd
        │                     ├── GraalPyRunner.java
        │                     ├── contracts
        │                     │         ├── Hello.java
        │                     │         ├── OptimizeService.java
        │                     │         └── RequestHandler.java
        │                     ├── exceptions
        │                     │         └── GraalPyExecutionException.java
        │                     ├── helpers
        │                     │         ├── PolyglotHelper.java
        │                     │         └── PythonExecutor.java
        │                     └── utils
        │                         ├── CommonUtils.java
        │                         ├── Constants.java
        │                         └── StringCaseConverter.java
        ├── python
        │         ├── hello.py
        │         ├── optimize_service.py
        │         └── request_handler.py
        └── resources

```

## Contributing
Contributions are welcome! Please fork this repository and submit pull requests.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
