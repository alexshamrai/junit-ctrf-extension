# JUnit CTRF Reporter

[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexshamrai/junit-ctrf-reporter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.alexshamrai%22%20AND%20a:%22junit-ctrf-reporter%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A JUnit 5 library that generates test reports following the standardized [CTRF (Common Test Report Format)](https://ctrf.io/) specification, providing both a JUnit Jupiter Extension and a JUnit Platform TestExecutionListener.

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
    implementation 'io.github.alexshamrai:junit-ctrf-reporter:0.4.0'
}
```

#### Maven

Add to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.alexshamrai</groupId>
    <artifactId>junit-ctrf-reporter</artifactId>
    <version>0.4.0</version>
</dependency>
```

## Usage options

There are two ways to use the CTRF reporter: as a JUnit Jupiter Extension or as a JUnit Platform TestExecutionListener.

### 1. Use CtrfListener (JUnit Platform TestExecutionListener)
This approach is suitable when you need to enable CTRF reporting globally for all tests executed by the JUnit Platform, without modifying individual test classes.
 Register the listener by creating a file named org.junit.platform.launcher.TestExecutionListener in your src/test/resources/META-INF/services directory and add the fully qualified name of the listener:
```
io.github.alexshamrai.launcher.CtrfListener
```

Programmatically:
If you are invoking the JUnit Platform Launcher directly, you can register the listener programmatically:
``` java
// Example with LauncherDiscoveryRequest
LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
    .selectors(selectPackage("com.example"))
    .build();

Launcher launcher = LauncherFactory.create();
launcher.registerTestExecutionListeners(new CtrfListener());
launcher.execute(request);
```

### 2. Register CtrfExtension

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
It allows you to configure a report only for custom classes
For more details on JUnit extensions, see the [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/#extensions).

### Configure Properties

You can configure the CTRF extension in two ways:

1. **Using a properties file**: Create a `ctrf.properties` file in your project's `src/test/resources` directory.
2. **Using system properties**: Pass configuration values as JVM system properties using `-D` parameters.

#### Properties File Example

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

#### Using System Properties

You can override any property by passing it as a system property. For example:

```
-Dctrf.report.path=custom/path/report.json
-Dctrf.app.name="My Custom App Name"
```

**Note:**

You need to ensure that system properties are passed to the test task by adding the following to your `build.gradle` file:

```groovy
test {
    // This ensures "ctrf" system properties are passed to the test
    systemProperties += System.properties.findAll { k, v -> k.toString().startsWith("ctrf") }
}
```

## Configuration Reference

The following parameters can be configured in the `ctrf.properties` file:

| Parameter                         | Description                                                  | Default Value      |
|-----------------------------------|--------------------------------------------------------------|--------------------|
| `ctrf.report.path`                | The file path where the CTRF report will be saved            | `ctrf-report.json` |
| `ctrf.max.message.length`         | Maximum length for error messages in the report              | `500`              |
| `ctrf.calculate.startup.duration` | Whether to calculate and include test suite startup duration | `false`            |
| `junit.version`                   | The version of JUnit used in your project                    |                    |
| `ctrf.report.name`                | Name of the test report                                      |                    |
| `ctrf.app.name`                   | Name of the application under test                           |                    |
| `ctrf.app.version`                | Version of the application under test                        |                    |
| `ctrf.build.name`                 | Name or ID of the build                                      |                    |
| `ctrf.build.number`               | Build number from your CI/CD system                          |                    |
| `ctrf.build.url`                  | URL to the build in your CI/CD system                        |                    |
| `ctrf.repository.name`            | Name of the source code repository                           |                    |
| `ctrf.repository.url`             | URL to the source code repository                            |                    |
| `ctrf.commit`                     | Commit hash of the code being tested                         |                    |
| `ctrf.branch.name`                | Name of the source control branch                            |                    |
| `ctrf.os.platform`                | Operating system platform                                    |                    |
| `ctrf.os.release`                 | OS release identifier                                        |                    |
| `ctrf.os.version`                 | OS version details                                           |                    |
| `ctrf.test.environment`           | Test environment identifier (e.g., dev, staging, prod)       |                    |

All mandatory parameters have default values

## Contributing

Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute to this project.

## Development

For information on setting up the project locally for development, see [DEVELOPMENT.md](DEVELOPMENT.md).

## Automated Release to Maven Central

This project uses GitHub Actions to automatically publish releases to Maven Central. The release process is fully automated and can be triggered either by pushing a version tag or manually through the GitHub Actions UI.

### Prerequisites

Before you can use the automated release workflow, you need to configure the following GitHub secrets in your repository:

#### Required Secrets

1. **`MAVEN_CENTRAL_USERNAME`** - Your Maven Central username (user token)
2. **`MAVEN_CENTRAL_PASSWORD`** - Your Maven Central password (user token password)
3. **`GPG_PRIVATE_KEY`** - Your GPG private key in ASCII-armored format
4. **`GPG_PASSPHRASE`** - The passphrase for your GPG private key

### Setting Up GitHub Secrets

#### 1. Maven Central Credentials

1. Log in to [Maven Central](https://central.sonatype.com/)
2. Generate a user token:
   - Go to your account settings
   - Navigate to "Generate User Token"
   - Save the username and password provided
3. Add these to GitHub Secrets:
   - `MAVEN_CENTRAL_USERNAME`: The token username
   - `MAVEN_CENTRAL_PASSWORD`: The token password

#### 2. GPG Signing Keys

If you don't have a GPG key yet, create one:

```bash
# Generate a new GPG key
gpg --gen-key

# Follow the prompts to:
# - Select key type (RSA and RSA is recommended)
# - Set key size (4096 bits recommended)
# - Set expiration (or no expiration)
# - Provide your name and email
# - Set a strong passphrase
```

Export your GPG key in ASCII-armored format:

```bash
# List your keys to find the key ID
gpg --list-secret-keys --keyid-format LONG

# Export the private key in ASCII-armored format
# Replace <KEY_ID> with your actual key ID (e.g., the 16-character hex after 'sec')
gpg --armor --export-secret-keys <KEY_ID>
```

The output will look like:
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

lQdGBF5... (many lines of encoded key data)
...
-----END PGP PRIVATE KEY BLOCK-----
```

**Important:** Copy the entire output including the BEGIN and END lines.

Upload your public key to a key server:

```bash
# Replace <KEY_ID> with your key ID
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
```

Add to GitHub Secrets:
- `GPG_PRIVATE_KEY`: Paste the entire ASCII-armored private key (including BEGIN/END lines)
- `GPG_PASSPHRASE`: The passphrase you set when creating the key

#### 3. Adding Secrets to GitHub

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add each of the four secrets listed above

### Triggering a Release

There are two ways to trigger an automated release:

#### Option 1: Push a Version Tag (Recommended)

```bash
# Create and push a version tag
git tag v0.4.0
git push origin v0.4.0
```

The workflow will automatically:
1. Extract the version from the tag (e.g., `v0.4.0` → `0.4.0`)
2. Build the project
3. Sign the artifacts with your GPG key
4. Publish to Maven Central

#### Option 2: Manual Dispatch

1. Go to the **Actions** tab in your GitHub repository
2. Select the **"Publish to Maven Central"** workflow
3. Click **"Run workflow"**
4. Enter the version number (e.g., `0.4.0`)
5. Click **"Run workflow"**

### Post-Release Steps

After the workflow completes successfully:

1. **Verify the publication**: Check the workflow summary for the artifact URL
2. **Wait for synchronization**: It may take 15-30 minutes for the artifact to appear on Maven Central
3. **Update version numbers**: Update `projectVersion` and `releaseVersion` in `build.gradle` for the next development cycle
4. **Create a GitHub Release** (optional): Document the changes in a GitHub release

### Troubleshooting

**Workflow fails during signing:**
- Verify that `GPG_PRIVATE_KEY` includes the complete ASCII-armored key with BEGIN/END lines
- Ensure `GPG_PASSPHRASE` matches the passphrase used when creating the key
- Check that your public key was successfully uploaded to a key server

**Workflow fails during publication:**
- Verify your Maven Central credentials are correct
- Ensure you have claimed your namespace on Maven Central (e.g., `io.github.alexshamrai`)
- Check that the version number doesn't already exist

**Artifact doesn't appear on Maven Central:**
- Wait 15-30 minutes for synchronization
- Check [Maven Central Search](https://search.maven.org/) for your artifact
- Verify the workflow completed successfully without errors

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions, please [open an issue](https://github.com/alexshamrai/junit-ctrf-reporter/issues) on GitHub.
