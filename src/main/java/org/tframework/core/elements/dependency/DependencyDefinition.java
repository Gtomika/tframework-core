/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import java.lang.reflect.AnnotatedElement;

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
}
