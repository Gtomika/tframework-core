package org.tframework.core.ioc;

import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.InjectingAnnotations;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.annotations.NeedsTesting;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
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
    @NeedsTesting
    public static String getReferencedEntityName(Parameter parameter) {
        return getReferencedEntityName(parameter, parameter.getType().getName());
    }

    /**
     * Based on the annotation (or the lack of it) on a field, it will return which managed entity
     * it references as a dependency.
     * @param field The parameter to be checked.
     * @return The name of the dependency entity.
     */
    @NeedsTesting
    public static String getReferencedEntityName(Field field) {
        return getReferencedEntityName(field, field.getType().getName());
    }

    /**
     * Calculates the name that the {@link Managed} annotation on a class has.
     * @param managedEntityClass Class that has the annotation.
     * @return The name of the managed entity.
     */
    @NeedsTesting
    public static String getReferencedEntityName(Class<?> managedEntityClass) {
        return getReferencedEntityName(managedEntityClass, managedEntityClass.getName());
    }

    /**
     * Based on the annotation (or the lack of it) on a method, it will return which managed entity
     * it references as provider.
     * @param method The method to be checked.
     * @return The name of the provided entity.
     */
    @NeedsTesting
    public static String getProvidedEntityName(Method method) {
        return getReferencedEntityName(method, method.getReturnType().getName());
    }

    /**
     * Determines the managed entity name based on the {@link Managed} annotation's {@link Managed#name()} value.
     * @param annotatedElement The element with the {@link Managed} annotation.
     * @param defaultName Fallback value if no specific name was given in {@link Managed#name()}.
     * @return The referenced entity name.
     */
    @NeedsTesting
    private static String getReferencedEntityName(AnnotatedElement annotatedElement, String defaultName) {
        if(annotatedElement.isAnnotationPresent(Managed.class)) {
            Managed managedAnnotation = annotatedElement.getAnnotation(Managed.class);
            return managedAnnotation.name().equals(Managed.DEFAULT_MANAGED_NAME)
                    ? defaultName : managedAnnotation.name();
        } else {
            return defaultName;
        }
    }

    /**
     * Checks if the element has at least one of the injecting annotations: {@link InjectingAnnotations#getInjectingAnnotations()}.
     * @param annotatedElement The element to be checked.
     * @return True if there is at least one of the annotations present.
     */
    public static boolean hasAtLeastOneInjectingAnnotation(AnnotatedElement annotatedElement) {
        for(var injectingAnnotation: InjectingAnnotations.getInjectingAnnotations()) {
            if(annotatedElement.isAnnotationPresent(injectingAnnotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the list of classes contains a given annotation.
     * @return True if it is contained.
     */
    public static boolean classListContainsAnnotation(
            List<Class<? extends Annotation>> classList,
            Annotation annotation
    ) {
        for(Class<?> clazz: classList) {
            if(annotation.annotationType().equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
