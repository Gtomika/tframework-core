package org.tframework.core.ioc;

import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.Managed;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

    /**
     * Based on the annotation (or the lack of it) on a parameter, it will return which managed entity
     * it references as a dependency.
     * @param parameter The parameter to be checked.
     * @return The name of the dependency entity.
     */
    //TODO unit test
    public static String getReferencedEntityName(Parameter parameter) {
        if(parameter.isAnnotationPresent(Injected.class)) {
            Injected injected = parameter.getAnnotation(Injected.class);
            return injected.name().equals(Managed.DEFAULT_MANAGED_NAME) ? parameter.getType().getName() : injected.name();
        } else {
            return parameter.getType().getName();
        }
    }

    /**
     * Calculates the name that the {@link Injected} annotation has. This will be by default the type of the
     * field it was placed on, or if {@link Injected#name()} was specified, then this custom name.
     * @param field The field the annotation was placed on.
     * @param injected The annotation.
     * @return The name of the injected entity.
     */
    public static String getReferencedEntityName(Field field, Injected injected) {
        return injected.name().equals(Managed.DEFAULT_MANAGED_NAME) ? field.getType().getName() : injected.name();
    }

    /**
     * Calculates the name that the {@link Managed} annotation on a class has.
     * @param managedEntityClass Class that has the annotation.
     * @return The name of the managed entity.
     */
    //TODO unit test
    public static String getReferencedEntityName(Class<?> managedEntityClass) {
        if(managedEntityClass.isAnnotationPresent(Managed.class)) {
            Managed managedAnnotation = managedEntityClass.getAnnotation(Managed.class);
            return managedAnnotation.name().equals(Managed.DEFAULT_MANAGED_NAME)
                    ? managedEntityClass.getName() : managedAnnotation.name();
        } else {
            return managedEntityClass.getName();
        }
    }
}
