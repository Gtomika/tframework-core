# Properties

Properties are a way to store external configuration for your application. There are several ways to set properties.
A TFramework property can be single valued, or a list of values.

## Property name rules and recommendations

There are no restrictions on property names, other than the fact that the name must not be empty. However, the 
recommendation is to group your properties by using a prefix. For example, if you want to store database related
properties, you can use the `db` prefix. This way, you can easily distinguish between different property groups.

It is also recommended to separate the groups inside a property name with the `.` character. To continue with the 
previous example, you can use `db.url`, `db.username`, `db.password` etc. This way, it will be clear that these 
properties are related.

## How to set properties

As a high level overview, properties can be set from files, or one-by-one.

### Using property files

The recommended way to set properties is to use property files. Property files are single-document YAML files, that are 
placed inside the application resources directory. For your convenience, some property files are activated by default, if 
detected. These are:

- `properties.yaml` (this is the "base" property file)
- `properties-default.yaml`

Furthermore, if a profile is set, then the `properties-{profile}.yaml` file is also activated by default. For example,
if the `dev` profile is set, then the `properties-dev.yaml` file is activated (if present).

Here is an example property file:

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

> :gear: **Technical note**: The *SnakeYAML* library is used for parsing the YAML files.

If none of these files suit your needs, you can create your own property file in the resources folder. Below are the ways 
how custom property files can be activated. Multiple ways can be combined.

#### Custom property file from environment variable

You can also set a custom property file by using the `TFRAMEWORK_PROPERTY_FILES` environment variable. The value of this 
variable should be one or more file paths, separated by commas. For example:

```
TFRAMEWORK_PROPERTY_FILES=someDir/custom-properties.yaml,anotherDir/another-properties.yaml
```

As you can see, the paths should be relative to the resources directory.

> :gear: **Technical note**: See the [EnvironmentPropertyFileScanner](../src/main/java/org/tframework/core/properties/filescanners/EnvironmentPropertyFileScanner.java)

#### Custom property file from system property

You can also set custom property files by using the `tframework.propertyFiles` system property. The logic is 
the same as with the environment variables.

> :gear: **Technical note**: See the [SystemPropertyFileScanner](../src/main/java/org/tframework/core/properties/filescanners/SystemPropertyFileScanner.java)

#### Custom property file from CLI arguments

Another way to set custom property files is by using command line arguments. The key is `tframework.propertyFiles`, which 
must be followed by an `=`, then the paths to the property files. The logic here is the same as with the other methods.

> :gear: **Technical note**: See the [CliArgumentPropertyFileScanner](../src/main/java/org/tframework/core/properties/filescanners/CliArgumentPropertyFileScanner.java)

#### Property precedence from files

If a property is set in multiple files, the reading precedence is as follows:

1. `properties.yaml`
2. `properties-{profile}.yaml`
3. Custom property files (in the order they are set)

Properties that appear later in the list will override properties that appear earlier. For example, you can override properties 
from the default file, by setting the same property in the profile specific file or a custom file.

### Setting properties one-by-one

Properties can also be set one-by-one. You may use environment variables, system properties or command line arguments to set
individual properties. This is more bothersome than using property files, and usually not needed.

> :gear: **Technical note**: See the [property scanners package](../src/main/java/org/tframework/core/properties/scanners)
> for more details on how individual properties can be set, and what environment variable and system property names to use.

Please note that individual properties set this way will override properties set in property files.

## Using properties

Once a property is set, it can be used in the application. The most common use case is injecting the property value into 
elements.

### Injecting properties into elements

You can use the `@InjectProperty("property.name")` annotation to inject a property into an element. Let's take a look at 
an example element. Here is a way how to inject a properties into it:

```java
@Element
public class CoolElement {

    //field injection
    @InjectProperty("some.cool.property")
    private String someCoolProperty;
    
    @PostInitialization
    public void doSomething() {
        System.out.println("Some cool property is: " + someCoolProperty);
    }
}
```

Equivalently, you can use constructor injection to achieve the same:

```java
@Element
public class CoolElement {

    private final String someCoolProperty;

    public CoolElement(@InjectProperty("some.cool.property") String someCoolProperty) {
        this.someCoolProperty = someCoolProperty;
    }
    
    @PostInitialization
    public void doSomething() {
        System.out.println("Some cool property is: " + someCoolProperty);
    }
}
```

### Getting properties from container

The element `PropertiesContainer` is always available in the application. You can use it to get properties directly.
It is recommended to inject properties into elements, rather than getting them from the container.

The source of the properties container can be seen here: [PropertiesContainer](../src/main/java/org/tframework/core/properties/PropertiesContainer.java)

## Property conversion

Properties are always stored as strings, or list of strings, in case of multi-valued properties. However, we often need to 
inject these properties into objects of other types: most commonly integers or booleans. The framework supports automatic
conversion of properties to some other types. There are:

- `String` (no conversion needed)
- `List<String>`
- `int` and `Integer`
- `boolean` and `Boolean`

Do note that injecting a property into a field of a type that is not supported will result in an `PropertyConverterNotFoundException`.
Also, if the property value cannot be converted to the target type, a `PropertyConversionException` will be thrown. Let's assume we 
have the following property file:

```yaml
some:
  cool:
    property1: 42
    property2: true
```

Here is how you can inject these properties into an element, where the conversion will be done automatically:

```java
@Element
public class CoolElement {

    @InjectProperty("some.cool.property1")
    private int someCoolProperty1; //may use wrapper type Integer as well
    
    @InjectProperty("some.cool.property2")
    private boolean someCoolProperty2; //may use wrapper type Boolean as well
    
    @PostInitialization
    public void doSomething() {
        System.out.println("Some cool property 1 is: " + someCoolProperty1);
        System.out.println("Some cool property 2 is: " + someCoolProperty2);
    }
}
```

A current **limitation** of the framework is that not all types are supported for automatic conversion. Also, it is 
not possible to add a custom converter at the moment. However, you can always inject the property as a string, and
convert it manually.

> :gear: **Technical note**: See the [property converter package](../src/main/java/org/tframework/core/properties/converters)

## Next steps

Now that you understand properties, you can move on to the [elements document](./elements.md).
This document explains the most interesting feature of the framework: elements.