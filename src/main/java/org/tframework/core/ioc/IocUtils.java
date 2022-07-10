package org.tframework.core.ioc;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Various utility methods related to IoC that do not belong to any other class.
 */
public class IocUtils {

    /**
     * Creates a name for the method, which makes it easily identifiable for the user. This is done 
     * by concatenating the class and method names. For example, if {@link String#length()} is passed here, 
     * the returned string will be 'java.lang.String#length
     * @param method The method that will be named.
     * @return The concatenated name.
     */
    public static String createClassAndMethodName(Method method) {
        Objects.requireNonNull(method);
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
    
}
