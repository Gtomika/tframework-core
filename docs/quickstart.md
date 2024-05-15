# Quickstart guide

This guide will show you step-by-step how to start using the framework. A complete 
example application is also provided.

## Requirements:

Before you start, ensure you have the following installed:

- Java 21 or higher
- Gradle (any version that supports Java 21, such as Gradle 8.5)

## Setting up the project

Create a new gradle project. You can use `gradle init` to do so. Any decent 
IDE will also support creating a new gradle project.

In the `build.gradle` file, add Jitpack as a repository:

```groovy
repositories {
    mavenCentral()
    maven {
        url "https://jitpack.io"
    }
}
```

If you need any other repositories, add them as well. For the dependencies, add the 
framework core, the test module and some logging library. For example:

```groovy
dependencies {
    implementation "com.github.Gtomika:tframework-core:${tframeworkVersion}"
    implementation "org.apache.logging.log4j:log4j-slf4j2-impl:${log4jVersion}"

    testImplementation "com.github.gtomika:tframework-test:${tframeworkTestVersion}"
    testImplementation platform("org.junit:junit-bom:${junitVersion}")
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```

In order for this guide to not become outdated, the versions are placeholders. The versions of 
the framework can be taken from the releases page of the [core module](https://github.com/Gtomika/tframework-core/releases) and the [test module](https://github.com/Gtomika/tframework-test/releases).
As for the other dependencies, you can find the versions on the respective websites.

## Optional - seeing some logs

The framework uses SLF4J for logging. You can use any logging library that is compatible with SLF4J.
In the example above, the log4j implementation is used. If you want to see some logs, you
can add a `log4j2.xml` file to the `src/main/resources` directory. Here is a decent example 
that will show some nicely formatted logs:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd} | %d{HH:mm:ss.SSS} | %15.15t | %-5level | %50.50logger | %msg %replace{%mdc}{\{\}}{}%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

To see more logs from the framework, you can set `<Root level="info">` to "debug" or "trace".

## Creating the application

In the `src/main/java` directory, create your base package. For example, `com.myapp`. In this package 
we will place the so called "root" class. This class will be the entry point of the application.

```java
@TFrameworkRootClass
public class ExampleApp {

    public static void main(String[] args) {
        TFramework.start("example-app", ExampleApp.class, args);
    }
}
```

This class is annotated with `@TFrameworkRootClass`. This annotation is used to mark the
root class of the application. The `TFramework.start()` method is called with the name of the application,
the root class and the command line arguments.

It is **highly recommended** that your application is to have the root class in the top package of your application. 
Then all your classes and element declarations should be in either this package or its subpackages. This 
way, everything will be picked up automatically.

## Complete example project

A working example project is provided here:

https://github.com/Gtomika/tframework-example

This is a minimalistic web application that provides an API to read from a CSV file.

## Further reading

For more detailed information, see the other documents in the `docs` directory.

- [Profiles](./profiles.md)
- [Properties](./properties.md)
- [Elements](./elements.md)

