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
    implementation 'io.github.alexshamrai:junit-ctrf-extension:0.1.0'
}
```

#### Maven

Add to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.alexshamrai</groupId>
    <artifactId>junit-ctrf-extension</artifactId>
    <version>0.1.0</version>
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