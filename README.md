# JUnit CTRF Extension

[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexshamrai/junit-ctrf-extension.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.alexshamrai%22%20AND%20a:%22junit-ctrf-extension%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A JUnit 5 extension that generates test reports following the standardized [CTRF (Common Test Report Format)](https://ctrf.io/) specification.

## What is CTRF?

CTRF is a universal JSON test report schema that addresses the lack of a standardized format for JSON test reports.

**Consistency Across Tools:** Different testing tools and frameworks often produce reports in varied formats. CTRF ensures a uniform structure, making it easier to understand and compare reports, regardless of the testing tool used.

**Language and Framework Agnostic:** It provides a universal reporting schema that works seamlessly with any programming language and testing framework.

**Facilitates Better Analysis:** With a standardized format, programmatically analyzing test outcomes across multiple platforms becomes more straightforward.

## Features

- Generates CTRF-compliant JSON test reports for JUnit 5 tests
- Captures test status, duration, and failure details
- Supports customization of report content via configuration
- Tracks and includes environment information
- Handles parallel test execution

## Getting Started

### Add Dependency

To use the JUnit CTRF Extension in your project, add the following dependency:

#### Gradle

Add to your `build.gradle` file:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.alexshamrai:junit-ctrf-extension:0.3.1'
}
```

#### Maven

Add to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.alexshamrai</groupId>
    <artifactId>junit-ctrf-extension</artifactId>
    <version>0.3.1</version>
</dependency>
```

### Register JUnit Extension

To use the JUnit CTRF Extension, you need to register it in your JUnit test class. You can do this by adding the `@ExtendWith` annotation to your test class:

```java
@ExtendWith(CtrfExtension.class)
public class MyTest {
    
    @Test
    void testExample() {
        // Your test code
    }
}
```

For more details on JUnit extensions, see the [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/#extensions).

### Configure Properties

Create a `ctrf.properties` file in your project's `src/test/resources` directory to customize the CTRF report:

```properties
ctrf.report.path=build/test-results/test/ctrf-report.json
ctrf.max.message.length=300

junit.version=5.11.3

ctrf.report.name=My Report
ctrf.app.name=My Application
ctrf.app.version=1.0.0
ctrf.test.environment=staging

ctrf.build.name=feature-branch
ctrf.build.number=123
ctrf.build.url=http://ci.example.com/build/123
ctrf.repository.name=my-repo
ctrf.repository.url=http://github.com/my-repo
ctrf.commit=9aba36cedaab8d9404eebedeba3739c55af83a01
ctrf.branch.name=main

ctrf.os.platform=Linux
ctrf.os.release=5.4.0-42-generic
ctrf.os.version=Ubuntu 20.04
```

## Configuration Reference

The following parameters can be configured in the `ctrf.properties` file:

| Parameter                 | Description                                            | Default Value      |
|---------------------------|--------------------------------------------------------|--------------------|
| `ctrf.report.path`        | The file path where the CTRF report will be saved      | `ctrf-report.json` |
| `ctrf.max.message.length` | Maximum length for error messages in the report        | `500`              |
| `junit.version`           | The version of JUnit used in your project              |                    |
| `ctrf.report.name`        | Name of the test report                                |                    |
| `ctrf.app.name`           | Name of the application under test                     |                    |
| `ctrf.app.version`        | Version of the application under test                  |                    |
| `ctrf.build.name`         | Name or ID of the build                                |                    |
| `ctrf.build.number`       | Build number from your CI/CD system                    |                    |
| `ctrf.build.url`          | URL to the build in your CI/CD system                  |                    |
| `ctrf.repository.name`    | Name of the source code repository                     |                    |
| `ctrf.repository.url`     | URL to the source code repository                      |                    |
| `ctrf.commit`             | Commit hash of the code being tested                   |                    |
| `ctrf.branch.name`        | Name of the source control branch                      |                    |
| `ctrf.os.platform`        | Operating system platform                              |                    |
| `ctrf.os.release`         | OS release identifier                                  |                    |
| `ctrf.os.version`         | OS version details                                     |                    |
| `ctrf.test.environment`   | Test environment identifier (e.g., dev, staging, prod) |                    |

All mandatory parameters have default values

## Contributing

Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute to this project.

## Development

For information on setting up the project locally for development, see [DEVELOPMENT.md](DEVELOPMENT.md).

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions, please [open an issue](https://github.com/alexshamrai/junit-ctrf-extension/issues) on GitHub.
