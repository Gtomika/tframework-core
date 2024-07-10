/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.dependency.DependencyDefinition;

/**
 * Utilities to get types of generic classes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypeUtils {

    /**
     * Gets the <b>first</b> type parameter of a genetic dependency definition.
     * @param dependencyDefinition The {@link DependencyDefinition} to get the type parameter from. This is assumed to
     *                             be a generic type with exactly one type parameter. For example
     *                             a {@link List} or a {@link Optional}.
     * @return The type parameter of the dependency definition. For example if the dependency definition is {@code List<String>},
     *        this method will return {@code String.class}.
     */
    public static Class<?> getTypeParameter(DependencyDefinition dependencyDefinition) {
        return getTypeParameter(dependencyDefinition, 0);
    }

    /**
     * An extended version of {@link #getTypeParameter(DependencyDefinition)} which supports getting
     * any type parameters of a dependency.
     */
    public static Class<?> getTypeParameter(DependencyDefinition dependencyDefinition, int index) {
        ParameterizedType parameterizedType = switch (dependencyDefinition.annotationSource()) {
            case Field field -> (ParameterizedType) field.getGenericType();
            case Parameter parameter -> (ParameterizedType) parameter.getParameterizedType();
            default -> throw new IllegalArgumentException("Unsupported annotation source: " + dependencyDefinition.annotationSource());
        };
        return (Class<?>) parameterizedType.getActualTypeArguments()[index];
    }
}
