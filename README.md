![CI pipeline](https://github.com/Gtomika/tframework-core/actions/workflows/ci-pipeline.yaml/badge.svg)

# TFramework

This is the repository of the TFramework, which is an application development framework for Java.
**It is a hobby project** and not meant for serious use.

```gradle
repositories {
     ...
     maven {
        url "https://jitpack.io"
     }
}

dependencies {
    implementation "com.github.Gtomika:tframework-core:${tframeworkVersion}"
}
```

The versions can be taken from the releases page of the [core module](https://github.com/Gtomika/tframework-core/releases)
and the [test module](https://github.com/Gtomika/tframework-test/releases).

## Features

This is a high level overview of the features of the framework. For more detailed information, linked
documents (which are in the `docs` directory).

### Profiles

The framework supports the concept of profiles. Setting a profile has an
effect on the configuration of the application. There are several ways to set a profile:

* The profile `default` is always set.
* The `TFRAMEWORK_PROFILES` environment variable can be set to a comma separated list of profiles.
* The `tframework.profiles` system property can be set to a comma separated list of profiles.
* Command line arguments can be provided in the form of `tframework.profiles=profile1,profile2,...`.

Profiles can activate property files and elements.

For more details, see the [profiles document](./docs/profiles.md).

### Properties

Properties are key-value pairs that can be provided as YAML files in the application resources.
For example, the following YAML file defines several properties:

```yaml
some:
  cool:
    property1: value1
    property2: value2
nice-list:
  - one
  - two
  - three
```

This will add the `some.cool.property1` and `some.cool.property2` properties with the values `value1` and `value2` respectively.
It will also add the `nice-list` property with the value `["one", "two", "three"]`.

For more details, see the [properties document](./docs/properties.md).

### Elements

The framework supports the concept of **elements**, which are similar to what *beans* are in
Spring. These are components managed by the framework, have different names, types and scopes and
can be injected into each other.

```java
@Element
public class MyElement {

    private final OtherElement otherElement;

    public MyElement(OtherElement otherElement) {
        this.otherElement = otherElement;
    }

    public void doSomething() {
        otherElement.doSomethingElse();
    }
}

```

Custom package scanning, constructor and field injection, element lifecycle management and more are supported.
For all the details on how to scan, declare and inject elements, see the [elements document](./docs/elements.md).

### How to run

The framework should be started from the `main` method:

```java
@TFrameworkRootClass
public class MyApplication {

    public static void main(String[] args) {
        Application app = TFramework.start("My cool app", MyApplication.class, args);
    }
}
```

## Additional documents

- [About versioning and release](/docs/versioning_and_release.md)
