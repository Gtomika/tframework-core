/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import lombok.NonNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * A dependency definition is a combination of the annotated element that defines the dependency and the type of the
 * dependency. For example, in case of:
 * <pre>{@code
 * @InjectElement("someDependency")
 * public String someField;
 * }</pre>
 * the dependency definition would be:
 * <ul>
 *     <li>Annotation source: the {@code java.lang.reflect.Field} object.</li>
 *     <li>Dependency type: {@code Class<java.lang.String>}</li>
 * </ul>
 * @param annotationSource The annotated element that defines the dependency.
 * @param dependencyType The type of the dependency.
 */
public record DependencyDefinition(
        AnnotatedElement annotationSource,
        Class<?> dependencyType
) {

    /**
     * Creates a dependency definition from a {@link Parameter}.
     * @param parameter The parameter to create the dependency definition from.
     */
    public static DependencyDefinition fromParameter(@NonNull Parameter parameter) {
        return new DependencyDefinition(parameter, parameter.getType());
    }

    /**
     * Creates a dependency definition from a {@link Field}.
     * @param field The field to create the dependency definition from.
     */
    public static DependencyDefinition fromField(@NonNull Field field) {
        return new DependencyDefinition(field, field.getType());
    }

}
