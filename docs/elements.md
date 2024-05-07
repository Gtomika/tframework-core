# Elements

Elements are the building blocks of a TFramework application. They are managed entities, similar to how 
beans work in Spring. Element instances are created by the framework and are injected into other elements as needed.

## Scanning elements

Elements are discovered by the framework by scanning certain packages of the classpath. The **highly recommended** way to structure
your application is to have the root class (the one annotated with `TFrameworkRootClass` and housing the `TFramework.start()` method) 
in the top package of your application. Then all your classes and element declarations should be in either this package or its subpackages.

**If you structure your application this way, you may skip to the next section: Declaring elements. Your elements will be scanned automatically.**

> :gear: **Technical note:** The [Classgraph](https://github.com/classgraph/classgraph) library is powering the scanning mechanism.
> It is a great and fast library for inverted reflections.

In case you need to scan other packages or classes, it can be done. See below for the provided ways.

### Scanning packages

List any additional packages to scan with the property `org.tframework.elements.scan-packages`. This property takes a list.
For example, to do so with a properties file:

```yaml
org:
  tframework:
    elements:
      scan-packages:
        - com.myapp.elements
        - com.myapp.other.elements
```

You may of course use any other ways to set this property, as described in the [properties](properties.md) document.
Please note that only valid packages that are on the classpath will be accepted here. If you provide an invalid package name,
the framework will log an error and ignore it.

### Scanning individual classes

If you need to scan individual classes, you can do so with the property `org.tframework.elements.scan-classes`. This property takes a list.
For example, to do so with a properties file:

```yaml
org:
  tframework:
    elements:
      scan-classes:
        - com.myapp.elements.MyElement
        - com.myapp.other.elements.OtherElement
```

Classes that could not be found on the classpath will be ignored. The framework will log a warning for each class that could not be found.
This method can be freely combined with scanning packages.

## Declaring elements

Elements are declared in the code using annotations. The `@Element` annotation is used to declare an element. Each element 
has a **unique name** and a **scope**. The `@Element` annotation has corresponding attributes for these properties.
None of the attributes are mandatory:

- `name` - The name of the element. If not provided, the name is derived from the class name.
- `scope` - The scope of the element. If not provided, the scope is singleton. See [ElementScope](../src/main/java/org/tframework/core/elements/ElementScope.java) for the available scopes.

Now let's see how to declare elements in the code. For now, we will only see how to declare them, not how to inject them.

### Declaring an element with a class

Elements can be declared by annotating a class with the `@Element` annotation. When you want to mark your own classes 
as elements, this is the recommended way to do it.

```java
@Element
public class MyElement {
    // Element implementation
}
```

However, we don't always have control over the classes we want to declare as elements.

### Declaring an element with a method

In case we cannot annotate the class directly, or we want multiple elements from the same class, we can declare elements with methods. 
Please note that the class that houses the methods must itself be an element. It is usually some sort of "configuration" class.

```java
@Element
public class MyConfig {

    @Element(name = "myElement")
    public MyElement createMyElement() {
        return new MyElement();
    }

    @Element(name = "myElement2", scope = ElementScope.PROTOTYPE)
    public MyElement createMyElement2() {
        return new MyElement();
    }
}
```

In the above example, we have declared two elements, `myElement` and `myElement2`. The first one is a singleton, while the second one is a prototype.
Note that we had to provide the `name` attribute. Otherwise, there would be a naming conflict.

### Element constructors

The framework needs to be able to make instances of elements. For this, it needs access to a **public** constructor.
If you have only one public constructor, it will be selected by default (no need to annotated it). If you have multiple 
public constructors, you need to mark one of them with the `@ElementConstructor` annotation.
Non-public constructors will be ignored by the framework.

```java
@Element
public class MyElement {

    private final String name;
    
    @ElementConstructor
    public MyElement(String name) {
        this.name = name;
    }
    
    public MyElement() {
        this.name = "default";
    }
}
```

Note that if your selected element constructor has parameters, those will be **constructor injected** by the framework.
This will be detailed in a later section.

## Injecting dependencies into elements

A most important feature of elements is that they can be injected into each other. Additionally, not only elements 
can be injected, but also properties, as it's described in the [properties](properties.md) document. Here, we will focus on 
only injecting elements.

### Constructor injection

The recommended way to inject dependencies into an element is through the constructor. This is called **constructor injection**.
By default, a constructor parameter will be injected by type.

```java
@Element
public class MyElement {

    private final OtherElement otherElement;
    
    public MyElement(OtherElement otherElement) {
        this.otherElement = otherElement;
    }
}
```

In some cases, you may want to inject an element by name (or we are forced to). This can be done by providing the
`@InjectElement` annotation.

```java
@Element
public class MyElement {

    private final OtherElement otherElement;
    
    public MyElement(@InjectElement("theOneSpecificElement") OtherElement otherElement) {
        this.otherElement = otherElement;
    }
}
```

### Field injection

Another way to inject elements is through fields. This is called **field injection**. This makes the code more concise, but
testing can be harder. The selected fields can be of **any visibility**, but they must be:

- **Non-final**: obviously, as they will be set by the framework after object creation. To inject into a final field, use constructor injection.
- **Non-static**: as elements are instance-based.

To mark a field for injection, use the `@InjectElement` annotation. Not annotated fields will be ignored by the framework.
Similarly to constructor injection, fields will be injected by type by default, but you can specify a name as well.

```java
@Element
public class MyElement {

    @InjectElement
    private SomeElement someElement;
    
    @InjectElement("other-element")
    private OtherElement otherElement;
}
```

Please note that field injected elements will be injected after the constructor is called. This means that the fields will be `null` in the constructor.
To do something with such fields right after object creation, you can use the `@PostInitialization` annotation.

```java
@Element
public class MyElement {

    @InjectElement
    private SomeElement someElement;
    
    public MyElement() {
        // someElement is null here
    }
    
    @PostInitialization
    public void init() {
        someElement.doSomething();
    }
}
```

More on element lifecycle callbacks can be found in a later section.

### Injecting by type

Injecting an element by type follows these rules:

1. If there is one element with the **exact** desired the type, it will be injected.
2. Otherwise, elements will be looked up that are **assignable** to the desired type. If there is only one such element, it will be injected.

In both cases, if there are multiple elements that could be injected, the framework will log an error and the application will not start.
In these cases, you need to provide the `@InjectElement` annotation to specify which element to inject.

> :gear: **Technical note:** See [ElementByTypeResolver](../src/main/java/org/tframework/core/elements/ElementByTypeResolver.java).

### Dependencies to avoid

The framework will not allow circular dependencies. If a circular dependency is detected, the framework will throw the `CircularDependencyException`.

> :gear: **Technical note:** The [JGraphT](https://jgrapht.org) library is used to detect circular dependencies in 
> the dependency graph.

Here are some examples of what is considered a circular dependency:

- A depends on B, and B depends on A.
- A depends on itself.

## Element lifecycle callbacks

Elements can have lifecycle callbacks. These are methods that are called at certain points in the element's lifecycle.

### Post-initialization

The `@PostInitialization` annotation marks a method to be called after the element is fully initialized. This means that all
fields are injected and the constructor has been called.

Such method must be:

- **Public**: as it will be called by the framework.
- **Non-static**: as elements are instance-based.
- Return type should be void, but it can be anything. The return value will be ignored.
- No parameters are allowed.

An example can be seen above in the field injection section.