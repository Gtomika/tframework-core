/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.readers.ResourceNotFoundException;

/**
 * Utility method to load classes and resources.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassLoaderUtils {

    /**
     * Load a resource as a {@link String}.
     * @param resourceName Name of the resource (relative path in the {@code resources} directory).
     * @param callingClass Class that this method was called from.
     * @return Contents of the resource as a string.
     * @throws ResourceNotFoundException If resource with this name was not found.
     */
    public static String getResourceAsString(String resourceName, Class<?> callingClass) {
        try(InputStream inputStream = getResourceAsStream(resourceName, callingClass)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new ResourceNotFoundException(resourceName, e);
        }
    }

    /**
     * Load a resource as an {@link InputStream}.
     * @param resourceName Name of the resource (relative path in the {@code resources} directory).
     * @param callingClass Class that this method was called from.
     * @return Input stream opened to the resource.
     * @throws ResourceNotFoundException If resource with this name was not found.
     */
    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        if(url != null) {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new ResourceNotFoundException(resourceName, e);
            }
        } else {
            throw new ResourceNotFoundException(resourceName);
        }
    }

    /**
     * Loads a resource as a {@link URL}.
     * @author OpenSymphony project
     */
    private static URL getResource(String resourceName, Class<?> callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ClassLoaderUtils.class.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            ClassLoader cl = callingClass.getClassLoader();
            if (cl != null) {
                url = cl.getResource(resourceName);
            }
        }

        if ((url == null) && (resourceName != null) && ((resourceName.isEmpty()) || (resourceName.charAt(0) != '/'))) {
            return getResource('/' + resourceName, callingClass);
        }

        return url;
    }

    /**
     * Load a class, attempting the operation with different {@link ClassLoader}s.
     * @param className Class name to load.
     * @param callingClass Class that this method was called from.
     * @return The loaded class.
     * @throws ClassNotFoundException If class with {@code className} was not found with any class loaders.
     * @author OpenSymphony project
     */
    public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtils.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
        }
    }

    /**
     * Checks if the given class is available on the classpath.
     * @param className Class to check.
     * @param callingClass Class that this method was called from.
     * @return True only if {@code className} was found.
     */
    public static boolean isClassAvailable(String className, Class<?> callingClass) {
        try {
            loadClass(className, callingClass);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
