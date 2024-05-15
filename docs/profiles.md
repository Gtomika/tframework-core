# Profiles

Profiles can be used to apply different configurations to the TFramework application. A common use case is having 
different configurations for different environments (e.g. development, staging, production).

## Profile name rules

Please note that profiles names are case-sensitive, and can contain only english letters, numbers, hyphens and underscores.
Furthermore, their length must be between 1 and 50 characters. Short and concise names are recommended.

## How to set profiles

A profile will only be applied, if it's set (activated). There are several ways to activate a profile.

> :gear: **Technical note**: `ProfileScanner`s are responsible for detecting profiles. For details, see 
> the [related package](../src/main/java/org/tframework/core/profiles/scanners).

**You are free to combine these methods.** It is possible to set the same profile multiple times with different methods, but this 
does not have any additional effect: a profile is either set, or not set. It cannot be "set multiple times".

### Setting profiles as an environment variable

The `TFRAMEWORK_PROFILES` environment variable can be used to set profiles. The value of the environment variable can 
be a single profile, or a comma-separated list of profiles. For example, to set a single profile named `dev`:

```
TFRAMEWORK_PROFILES=dev
```

To set multiple profiles:

```
TFRAMEWORK_PROFILES=dev,test
```

If this variable is not set, this method of profile activation will not be used.

> :gear: **Technical note**: See the [EnvironmentProfileScanner](../src/main/java/org/tframework/core/profiles/scanners/EnvironmentProfileScanner.java)

### Setting profiles as a system property

Profiles can also be set as system properties. The system property key is `tframework.profiles`. The value of the system 
can be a single profile, or a comma-separated list of profiles. For example, to set a single profile named `dev`:

```
tframework.profiles=dev
```

To set multiple profiles:

```
tframework.profiles=dev,test
```

If this system property is not set, this method of profile activation will not be used.

> :gear: **Technical note**: See the [SystemPropertyProfileScanner](../src/main/java/org/tframework/core/profiles/scanners/SystemPropertyProfileScanner.java)

When observing the scanner shown in the technical note above, it can be seen that actually every system property 
that **begins** with `tframework.profiles` is scanned. This mechanism allows you to set profiles with different keys, 
however, this is mostly for internal use.

### Setting profiles as command line arguments

Profiles may be specified as command line arguments. The argument key is `tframework.profiles`. Then, the argument 
must continue with `=`, and finally a comma separated list of profiles. For example, to set profiles named `dev` and test, 
pass the following argument:

```
tframework.profiles=dev,test
```

This command line argument can be repeated.

> :gear: **Technical note**: See the [CLIProfileScanner](../src/main/java/org/tframework/core/profiles/scanners/CLIProfileScanner.java)

### "Setting" the default profile

The `default` profile is always set. This is to ensure that the application always has a profile.

> :gear: **Technical note**: See the [DefaultProfileScanner](../src/main/java/org/tframework/core/profiles/scanners/DefaultProfileScanner.java)

## The effects of profiles

### Profile specific properties files

Profiles can be used to activate or deactivate certain parts of the application. For example, a profile can be used to
activate a specific property file. It is known that the basic properties file `properties.yaml` is always loaded, if 
found in the resources. However, if a profile named `dev` is set, then the `properties-dev.yaml` file will also be loaded, 
and this applies to any profile. Consequently, since the `default` profile is always set, the `properties-default.yaml` 
will also always be loaded.

Please note the following:

- Just because a profile is set, it doesn't mean that the related `properties-<profile>.yaml` file must be present. 
  If the file is not found, it will be ignored.
- The properties found in the profile specific file will override the properties found in the basic properties file.
  They can also override each other, if you set the same property in multiple files, and all related profiles are set.

> :gear: **Technical note**: See the [ProfilesPropertyFileScanner](../src/main/java/org/tframework/core/properties/filescanners/ProfilesPropertyFileScanner.java)

### Profile specific elements

Elements can be profile specific. This means that an element can be activated or deactivated based on the profiles set. 
By default, an element does not care about profiles, and is always active. However, annotations can be used to make an
element profile specific.

The `@RequiredProfile` annotation can be used to specify the profiles that are required for the element to be active. This may 
be placed on the element class, or on the method that creates the element.

```java
@Element
@RequiredProfile("dev")
public class SomeElement {
  //stuff
}
```

Similarly, we can disable an element if specific profiles are set. The `@ForbiddenProfile` annotation can be used for this purpose.
Let's see this time with a method level annotation:

```java
@Element
@ForbiddenProfile("test")
public SomeElement createSomeElement() {
  //stuff
}
```

You are free to place multiple of these annotations on the same element, and it is also possible to use both annotations 
at the same time, to specify required and forbidden profiles. They will be combined with a logical **AND** operation.
Logical **OR** operation is not supported currently.

> :gear: **Technical note**: This is implemented with "element context filtering".
> See the [ProfileElementContextFilter](../src/main/java/org/tframework/core/elements/context/filter/ProfileElementContextFilter.java)

## Getting profiles at runtime

If for some reason you need to access the profiles at runtime, the element `ProfilesContainer` can be used. This element
is always available, and can be injected into any other element. The `ProfilesContainer` is also available from the 
`Application` element. Here are some ways to get it.

1. Right after starting the application (not recommended):

```java
@Slf4j
@TFrameworkRootClass
public class MyApplication {

    public static void main(String[] args) {
        Application app = TFramework.start("My cool app", MyApplication.class, args);
        ProfilesContainer profilesContainer = app.getProfilesContainer();
        log.info("IS the 'dev' profile set: {}", profilesContainer.isProfileSet("dev"));
    }
}
```

2. Injecting the `ProfilesContainer` into another element:

```java
@Element
public class SomeElement {

    private final ProfilesContainer profilesContainer;

    public SomeElement(ProfilesContainer profilesContainer) {
        this.profilesContainer = profilesContainer;
    }

    public void checkProfile() {
        log.info("Is the 'dev' profile set: {}", profilesContainer.isProfileSet("dev"));
    }

}
```

## Next steps

Once you understand profiles, you can move on to the [properties document](./properties.md). This document explains 
how to set properties in the application, and how to access them from elements.