/* Licensed under Apache-2.0 2024. */
package org.tframework.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.dependency.DependencyDefinition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypeUtils {

    /**
     * Gets the type parameter of a genetic dependency definition.
     * @param dependencyDefinition The {@link DependencyDefinition} to get the type parameter from. This is assumed to
     *                             be a generic type with exactly one type parameter. For example
     *                             a {@link List} or a {@link Optional}.
     * @return The type parameter of the dependency definition. For example if the dependency definition is {@code List<String>},
     *        this method will return {@code String.class}.
     */
    public static Class<?> getTypeParameter(DependencyDefinition dependencyDefinition) {
        ParameterizedType parameterizedType = switch (dependencyDefinition.annotationSource()) {
            case Field field -> (ParameterizedType) field.getGenericType();
            case Parameter parameter -> (ParameterizedType) parameter.getParameterizedType();
            default -> throw new IllegalArgumentException("Unsupported annotation source: " + dependencyDefinition.annotationSource());
        };
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
