# Development Setup

This document provides instructions for setting up the JUnit CTRF Extension project for local development.

## Prerequisites

- Java 21 or higher
- Gradle 8.13 or higher for build management
- Git for version control

## Local Project Setup

### Clone the Repository

```bash
git clone https://github.com/alexshamrai/junit-ctrf-reporter.git
cd junit-ctrf-reporter
```

### Build the Project

Using Gradle:
```bash
./gradlew build
```

### IDE Setup

#### IntelliJ IDEA
* Go to IDEA settings:
`Settings > Editor > Code style > Java > Scheme gear button > Import Scheme`
* Import the file located in this repo using format IntelliJ IDEA code style XML:
`config/checkstyle/intellij_idea_codestyle.xml`

### Running Tests

The solution uses JUnit 5 for unit testing. Run the tests with:
```bash
./gradlew :test
```
For integration tests info refer to [INTEGRATION_TESTS.md](INTEGRATION_TESTS.md)

### Code Quality Checks

The project uses checkstyle for code quality. Run the checks with:

```bash
./gradlew checkstyleMain checkstyleTest
```

## Project Structure

- `src/main/java` - Source code
- `src/test/java` - Test code
- `src/test/resources` - Test resources and configuration files

## Releasing

This section is for maintainers who have permission to release new versions:

1. Update version in build files
2. Update documentation
3. Create a new release tag
4. Deploy to Maven Central
