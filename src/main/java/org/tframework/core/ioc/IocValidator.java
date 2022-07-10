package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.ioc.annotations.Managed;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IocValidator {

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
     * </ul>
     * @param providerMethod The method which is annotated with {@link org.tframework.core.ioc.annotations.Managed},
     *                       and thus provides a managed entity.
     * @throws IllegalArgumentException If the method is invalid.
     */
    public static void validateProviderMethod(Method providerMethod) throws IllegalArgumentException {
        Objects.requireNonNull(providerMethod);
        if(!providerMethod.isAnnotationPresent(Managed.class)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not annotated by @Managed",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(!providerMethod.getDeclaringClass().isAnnotationPresent(Managed.class)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not in a managed class! Provider methods " +
                    "can only be declared inside classes annotated with @Managed", IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(providerMethod.getReturnType().equals(Void.TYPE)) {
            throw new IllegalArgumentException(String.format("Provider method '%s' has no return type, but this is mandatory!",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
        if(!Modifier.isPublic(providerMethod.getModifiers())) {
            throw new IllegalArgumentException(String.format("Provider method '%s' is not public!",
                    IocUtils.createClassAndMethodName(providerMethod)));
        }
    }

}
