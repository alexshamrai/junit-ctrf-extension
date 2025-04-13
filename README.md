# JUnit CTRF Extension

This is a JUnit 5 reporter to create test reports that follow the [CTRF standard](https://ctrf.io/).

## What is CTRF?

CTRF is a universal JSON test report schema that addresses the lack of a standardized format for JSON test reports.

**Consistency Across Tools:** Different testing tools and frameworks often produce reports in varied formats. CTRF ensures a uniform structure, making it easier to understand and compare reports, regardless of the testing tool used.

**Language and Framework Agnostic:** It provides a universal reporting schema that works seamlessly with any programming language and testing framework.

**Facilitates Better Analysis:** With a standardized format, programmatically analyzing test outcomes across multiple platforms becomes more straightforward.

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
    implementation 'io.github.alexshamrai:junit-ctrf-extension:0.3.0'
}
```

#### Maven

Add to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.alexshamrai</groupId>
    <artifactId>junit-ctrf-extension</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Register JUnit Extension

To use the JUnit CTRF Extension, you need to register it in your JUnit test class. You can do this by adding the `@ExtendWith` annotation to your test class and passing the `CtrfExtension` class:

```java
@ExtendWith(CtrfExtension.class)
public class MyTest {
    // Your test methods
}
```

See for more details: [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/#extensions)

### Configure Properties

Add a `ctrf.properties` file to your resources folder if you would like to use non-default parameters:

```properties
ctrf.report.path=build/test-results/test/ctrf-report.json
```

#### Configuration Parameters

The following parameters can be configured in the `ctrf.properties` file:

- `ctrf.report.path`: The path where the CTRF report will be saved. Default is `ctrf-report.json`.
- `ctrf.max.message.length`: The maximum length of messages in the report. Default is `500`.
- `junit.version`: The version of JUnit being used.
- `ctrf.report.name`: The name of the CTRF report.
- `ctrf.app.name`: The name of the application.
- `ctrf.app.version`: The version of the application.
- `ctrf.build.name`: The name of the build.
- `ctrf.build.number`: The number of the build.
- `ctrf.build.url`: The URL of the build.
- `ctrf.repository.name`: The name of the repository.
- `ctrf.repository.url`: The URL of the repository.
- `ctrf.commit`: The commit hash.
- `ctrf.branch.name`: The name of the branch.
- `ctrf.os.platform`: The operating system platform.
- `ctrf.os.release`: The release version of the operating system.
- `ctrf.os.version`: The version of the operating system.
- `ctrf.test.environment`: The test environment.

## Local Project Setup

### IntelliJ IDEA editor code style import
Go to IDEA settings:
`Settings > Editor > Code style > Java > Scheme gear button > Import Scheme`
Import the file located in this repo using format IntelliJ IDEA code style XML:
`config/checkstyle/intellij_idea_codestyle.xml`