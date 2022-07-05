/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.ioc.ManagedEntityScanner;

import java.util.stream.Collectors;

/**
 * The core class of the framework, which includes methods to start and stop a TFramework
 * application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class TFramework {

    /**
     * Start the TFramework application. This method is intended to be called from the {@code main} method.
     *
     * @param args Command line arguments, as received in the {@code main} method.
     */
    public static void start(String[] args) {
        log.info("Initializing TFramework application...");
        Class<?> rootClass = findClassAnnotatedWithTFrameworkRoot();
        log.info("Found root class: '{}'", rootClass.getName());
        ApplicationContext.initApplicationContext();
        ManagedEntityScanner.scanAndRegisterManagedEntities(rootClass);
    }

    /** Stops the TFramework application gracefully. */
    public static void stop() {
        //TODO
    }

    /**
     * Finds the class on the classpath that is annotated with {@link TFrameworkRoot}.
     * @return The class.
     * @throws TFrameworkRuntimeException If no (or multiple) class was found having this annotation.
     */
    private static Class<?> findClassAnnotatedWithTFrameworkRoot() throws TFrameworkRuntimeException {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
        );
        var candidates = reflections.getTypesAnnotatedWith(TFrameworkRoot.class);
        if(candidates.size() > 1) {
            throw new TFrameworkRuntimeException("Found multiple classes on the classpath annotated with @TFrameworkRoot, " +
                    "but only one is allowed. Classes found: " + candidates.stream().map(Class::getName).collect(Collectors.joining(",")));
        }
        return candidates.stream().findFirst().orElseThrow(() -> new TFrameworkRuntimeException("No class found on the classpath" +
                " annotated with @TFrameworkRoot. Exactly one such class is required."));
    }
}
