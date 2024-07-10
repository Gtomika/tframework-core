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
package org.tframework.core.elements.dependency;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import lombok.NonNull;

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
