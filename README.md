# TFramework

This is the repository of the TFramework, which is an application development framework for Java.
**It is a hobby project, and a work in progress**.

## Features

New features are added from time to time.

### Profiles

The framework supports the concept of profiles. Setting a profile has an
effect on the configuration of the application. There are several ways to set a profile:

* The profile `default` is always set.
* The `TFRAMEWORK_PROFILES` environment variable can be set to a comma separated list of profiles.
* The `tframework.profiles` system property can be set to a comma separated list of profiles.
* Command line arguments can be provided in the form of `tframework.profiles=profile1,profile2,...`.

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

There are several ways to provide property files:

* The file `properties.yaml` is always loaded without additional configuration.
* For any set profile `p`, the file `properties-p.yaml` is loaded automatically.
* The `TFRAMEWORK_PROPERTY_FILES` environment variable can be set to a comma separated list of property files.
* The `tframework.propertyFiles` system property can be set to a comma separated list of property files.
* Command line arguments can be provided in the form of `tframework.propertyFiles=file1,file2,...`.

Note that the property files should always be specified as a path relative to the application resources.

#### YAML parsing libraries

The framework does not impose a single YAML parser library. Instead, it uses the first available library on
the classpath from the following list:

_Jackson YAML module_: Include it with Gradle as:

```
implementation "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:[version]"'`
```
([latest version](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml)).

_Snake Yaml_: Include it with Gradle as:

```
implementation 'org.yaml:snakeyaml:[version]'`
```
([latest version](https://mvnrepository.com/artifact/org.yaml/snakeyaml)).

### Dependency Injection

This feature is still in development.

### How to run

Include the framework in your dependencies. If not present already,
also add one of the YAML parser libraries. For example, assuming you use Gradle:

```
TODO: public repo is not yet available
implementation 'org.yaml:snakeyaml:[version]'
```

Then, the framework should be started from the `main` method:

```java
public class MyApplication {

    public static void main(String[] args) {
        Application app = TFramework.start(args);
        //profiles and properties are available from the 'app' object
    }
}
```
