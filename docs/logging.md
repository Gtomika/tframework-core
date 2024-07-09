# Logging

Logging is an important part of all frameworks. It helps you to debug your application and to understand what 
is happening in your application. The TFramework prefers ease of use and simplicity. Therefore, everything 
related to logging will work out of the box if you include the TFramework in your project.

## SLF4J + Log4j2

The TFramework uses SLF4J as the logging facade. By default, the **Log4j2** implementation is used. A log4j2.xml
file is also included, which will show you well formatted logs with *INFO* log level. Such as:

```
2024-07-09 | 13:03:27.807 | main | INFO  | mework.core.initializers.CoreInitializationProcess | Starting TFramework core initialization... 
2024-07-09 | 13:03:27.822 | main | INFO  | ramework.core.initializers.ProfilesCoreInitializer | The profile initialization completed in 4 ms, and found the following profiles: [default] 
2024-07-09 | 13:03:27.828 | main | INFO  | mework.core.initializers.PropertiesCoreInitializer | Starting properties initialization... 
2024-07-09 | 13:03:27.847 | main | INFO  | mework.core.initializers.PropertiesCoreInitializer | The properties initialization completed in 19 ms, and found 2 properties. 
2024-07-09 | 13:03:28.157 | main | INFO  | mework.core.elements.ElementsInitializationProcess | Successfully assembled a total of 34 element contexts 
2024-07-09 | 13:03:28.172 | main | INFO  | mework.core.elements.ElementsInitializationProcess | A total of 34 element contexts survived after filtering 
2024-07-09 | 13:03:28.247 | main | INFO  | mework.core.elements.ElementsInitializationProcess | Successfully initialized 34 element contexts 
2024-07-09 | 13:03:28.247 | main | INFO  | ramework.core.initializers.ElementsCoreInitializer | The elements core initialization completed in 389 ms, and found 34 elements. 
2024-07-09 | 13:03:28.247 | main | INFO  | mework.core.initializers.CoreInitializationProcess | Successfully initialized the application 'example-app'! Let's get started!
```

You can add your own log4j2.xml file to the classpath, in `src/main/resources` folder. This will override
the default log4j2.xml file provided by the TFramework. You can use this to customize log levels and log formats, 
and anything else that log4j2 allows you to do.

## Different SLF4J implementations

If you want to use a different SLF4J implementation, you can exclude the default log4j2 dependency from 
the framework, and provide another implementation.

```groovy
//pull in the framework without logging implementation
implementation ("com.github.Gtomika:tframework-core:${tframeworkVersion}") {
    exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j2-impl'
}
//add your own logging implementation, in this case SLF4J Simple
implementation "org.slf4j:slf4j-simple:${slf4jSimpleVersion}"
```

Now you can completely customize your selected logging implementation.