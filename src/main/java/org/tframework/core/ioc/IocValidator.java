package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ClassUtils;
import org.tframework.core.ioc.annotations.InjectingAnnotations;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.annotations.NeedsTesting;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IocValidator {

    /**
     * Regex that matched only valid managed entity names.
     */
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z1-9.-]*");

    /**
     * Validates that a name for the managed entity is allowed. Only alphanumeric characters
     * and '.' and '-' characters are allowed.
     * @param name The name to validate.
     * @throws IllegalArgumentException If the name is not valid.
     * @return The name, if it is valid.
     */
    public static String validateEntityName(String name) throws IllegalArgumentException {
        if(!VALID_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(name);
        }
        return name;
    }

    /**
     * Validates that a provider method is allowed. For it to be allowed, it must satisfy these conditions:
     * <ul>
     *     <li>Be annotated with {@link org.tframework.core.ioc.annotations.Managed}.</li>
     *     <li>Be declared in another managed entity, a class also annotated with {@link org.tframework.core.ioc.annotations.Managed}.</li>
     *     <li>Must have a return type. A void method cannot provide anything.</li>
     *     <li>Must be public.</li>
     *     <li>Must not be static.</li>
     *     <li>If 'requiredReturnType' is provided, then the method must have return value of this type.</li>
     * </ul>
     * @param providerMethod The method which is annotated with {@link org.tframework.core.ioc.annotations.Managed},
     *                       and thus provides a managed entity.
     * @param requiredReturnType Type that this method must return. This parameter can be null, in which case the return
     *                           type is not checked against any type, only that is not null.
     * @throws IllegalArgumentException If the method is invalid.
     */
    public static void validateProviderMethod(Method providerMethod, @Nullable Class<?> requiredReturnType) throws IllegalArgumentException {
        Objects.requireNonNull(providerMethod);
        if(!providerMethod.isAnnotationPresent(Managed.class)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not annotated by @Managed",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(!providerMethod.getDeclaringClass().isAnnotationPresent(Managed.class)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not in a managed class! Provider methods " +
                    "can only be declared inside classes annotated with @Managed", IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(!Modifier.isPublic(providerMethod.getModifiers())) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not public, but this is required!",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(Modifier.isStatic(providerMethod.getModifiers())) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is static! This is not allowed!",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(providerMethod.getReturnType().equals(Void.TYPE)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' has no return type, but this is mandatory!",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        Class<?> returnType = providerMethod.getReturnType();
        //convert primitive to wrapper, to allow cases where return type is primitive
        if(returnType.isPrimitive()) returnType = ClassUtils.primitiveToWrapper(returnType);
        if(requiredReturnType != null && !returnType.equals(requiredReturnType)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' return type is expected to be '%s', but it is '%s'.",
                    IocUtils.createClassAndMethodName(providerMethod), requiredReturnType.getName(), providerMethod.getReturnType().getName()));
        }
    }

    /**
     * Validates that a field is allowed to be the target of injecting.
     * For it to be valid, these must be fulfilled:
     * <ul>
     *     <li>It must be in a managed entity.</li>
     *     <li>It must not be a static field.</li>
     *     <li>It must have exactly one of the {@link InjectingAnnotations#getInjectingAnnotations()} on it.</li>
     * </ul>
     * @param injectedField The field to be validated.
     * @return The injecting annotation found on this field.
     * @throws IllegalArgumentException If the field is not valid.
     */
    @NeedsTesting
    @Nullable
    public static Class<? extends Annotation> validateInjectedField(Field injectedField) throws IllegalArgumentException {
        if(!injectedField.getDeclaringClass().isAnnotationPresent(Managed.class)) {
            throw new IllegalArgumentException(String.format("Field '%s' in class '%s' is annotated with @Inject but it isn't " +
                    "in a managed entity. Field injection can only happen in managed entities annotated with @Managed.",
                    injectedField.getName(), injectedField.getDeclaringClass().getName()));
        }
        if(Modifier.isStatic(injectedField.getModifiers())) {
            throw new IllegalArgumentException(String.format("Field '%s' in class '%s' is annotated with @Inject bit it is static.",
                    injectedField.getName(), injectedField.getDeclaringClass().getName()));
        }
        return validateExactlyOneInjectingAnnotation(injectedField.getName(), injectedField);
    }

    /**
     * Validates a parameter that belongs to a provider method or a constructor used to create managed entities.
     * For it to be valid, it must have no annotation, or exactly one of these annotations:
     * {@link InjectingAnnotations#getInjectingAnnotations()}.
     * @param parameter The parameter to be validated.
     * @return If this parameter is valid and annotated, then returns the one valid annotation on it. If not annotated,
     * null is returned.
     * @throws IllegalArgumentException If the parameter is not valid.
     */
    @NeedsTesting
    @Nullable
    public static Class<? extends Annotation> validateProviderOrConstructorParameter(Parameter parameter) throws IllegalArgumentException {
        return validateAtMostOneInjectingAnnotation(parameter.getName(), parameter);
    }

    /**
     * Checks an element and determines if it has at most one of the injecting annotations:
     * the ones in {@link InjectingAnnotations#getInjectingAnnotations()}.
     * @param annotatedElementName Name of the field or parameter.
     * @param annotatedElement Element, for example a field or method.
     * @return The one injecting annotation present. Can be null if no annotation is present, this is allowed.
     * @throws IllegalArgumentException If there is more than one of the annotations present.
     */
    @Nullable
    @NeedsTesting
    private static Class<? extends Annotation> validateAtMostOneInjectingAnnotation(
            String annotatedElementName,
            AnnotatedElement annotatedElement
    ) throws IllegalArgumentException {
        Annotation[] annotations = annotatedElement.getAnnotations();
        if(annotations.length == 0) return null;
        int count = 0;
        Annotation foundAnnotation = null;
        for(Annotation annotation: annotations) {
            if(IocUtils.classListContainsAnnotation(InjectingAnnotations.getInjectingAnnotations(), annotation)) {
                foundAnnotation = annotation;
                count++;
            }
        }
        if(count == 0) { //all the annotations are not important here
            return null;
        } else if(count == 1) {
            return foundAnnotation.annotationType();
        } else {
            throw new IllegalArgumentException(String.format("Conflicting annotations found on parameter '%s'. Only one of these " +
                    "is allowed: %s", annotatedElementName, InjectingAnnotations.getInjectingAnnotations()));
        }
    }

    /**
     * Checks an element and determines if it has exactly one of the injecting annotations:
     * the ones in {@link InjectingAnnotations#getInjectingAnnotations()}.
     * @param annotatedElementName Name of the field or parameter.
     * @param annotatedElement Element, for example a field or method.
     * @return The one injecting annotation present. Can be null if no annotation is present, this is allowed.
     * @throws IllegalArgumentException If there is more or less than one of the annotations present.
     */
    @Nullable
    @NeedsTesting
    private static Class<? extends Annotation> validateExactlyOneInjectingAnnotation(
            String annotatedElementName,
            AnnotatedElement annotatedElement
    ) throws IllegalArgumentException {
        Annotation[] annotations = annotatedElement.getAnnotations();
        if(annotations.length == 0) return null;
        int count = 0;
        Annotation foundAnnotation = null;
        for(Annotation annotation: annotations) {
            if(IocUtils.classListContainsAnnotation(InjectingAnnotations.getInjectingAnnotations(), annotation)) {
                foundAnnotation = annotation;
                count++;
            }
        }
        if(count == 0) { //all the annotations are not important here
            throw new IllegalArgumentException(String.format("No injecting annotation found on element '%s'. Exactly one of these must " +
                    "be present: %s", annotatedElementName, InjectingAnnotations.getInjectingAnnotations()));
        } else if(count == 1) {
            return foundAnnotation.annotationType();
        } else {
            throw new IllegalArgumentException(String.format("Conflicting annotations found on element '%s'. Only one of these " +
                    "is allowed: %s", annotatedElementName, InjectingAnnotations.getInjectingAnnotations()));
        }
    }
}
