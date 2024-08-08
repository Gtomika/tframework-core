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
package org.tframework.core.elements.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * Contains methods to create {@link ElementContext}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementContextFactory {

    private static final ClassElementContextAssembler ASSEMBLER = ElementContextAssemblersFactory
            .createDefaultClassElementContextAssembler();

    /**
     * Creates an {@link ElementContext} from the given {@link Element} annotation and additional input.
     * @param elementName Name to use when creating the element. Provide {@link Element#NAME_NOT_SPECIFIED}
     *                    to have the name auto generated from type.
     * @param elementScope {@link ElementScope} of the element.
     * @param type The type of the element.
     * @param source The {@link ElementSource} of the element.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                  its own dependencies.
     */
    @TFrameworkInternal
    public static ElementContext from(
            String elementName,
            ElementScope elementScope,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        return switch (elementScope) {
            case SINGLETON -> new SingletonElementContext(
                    elementName,
                    type,
                    source,
                    dependencyResolutionInput
            );
            case PROTOTYPE -> new PrototypeElementContext(
                    elementName,
                    type,
                    source,
                    dependencyResolutionInput
            );
        };
    }

    /**
     * Creates a singleton {@link ElementContext} from the given type. Element name will
     * be derived from the type.
     * @param type The type of the element, cannot be null.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                 its own dependencies, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext singleton(
            @NonNull Class<?> type,
            @NonNull DependencyResolutionInput dependencyResolutionInput
    ) {
        return ASSEMBLER.assemble(
                ElementUtils.getElementNameByType(type),
                ElementScope.SINGLETON,
                type,
                dependencyResolutionInput
        );
    }

    /**
     * Creates a singleton {@link ElementContext} from the given type and name.
     * @param type The type of the element, cannot be null.
     * @param elementName The name of the element, cannot be null.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                 its own dependencies, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext singleton(
            @NonNull Class<?> type,
            @NonNull String elementName,
            @NonNull DependencyResolutionInput dependencyResolutionInput
    ) {
        return ASSEMBLER.assemble(
                elementName,
                ElementScope.SINGLETON,
                type,
                dependencyResolutionInput
        );
    }

    /**
     * Creates a singleton {@link ElementContext} from the given instance.
     * @param instance The instance of the element, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext singleton(@NonNull Object instance) {
        return PreConstructedElementContext.of(instance);
    }

    /**
     * Creates a singleton {@link ElementContext} from the given instance and name.
     * @param instance The instance of the element, cannot be null.
     * @param elementName The name of the element, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext singleton(@NonNull Object instance, @NonNull String elementName) {
        return PreConstructedElementContext.of(instance, elementName);
    }

    /**
     * Creates a prototype {@link ElementContext} from the given type. Element name will
     * be derived from the type.
     * @param type The type of the element, cannot be null.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                 its own dependencies, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext prototype(
            @NonNull Class<?> type,
            @NonNull DependencyResolutionInput dependencyResolutionInput
    ) {
        return ASSEMBLER.assemble(
                ElementUtils.getElementNameByType(type),
                ElementScope.PROTOTYPE,
                type,
                dependencyResolutionInput
        );
    }

    /**
     * Creates a prototype {@link ElementContext} from the given type and name.
     * @param type The type of the element, cannot be null.
     * @param elementName The name of the element, cannot be null.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                 its own dependencies, cannot be null.
     * @return The created {@link ElementContext}.
     */
    public static ElementContext prototype(
            @NonNull Class<?> type,
            @NonNull String elementName,
            @NonNull DependencyResolutionInput dependencyResolutionInput
    ) {
        return ASSEMBLER.assemble(
                elementName,
                ElementScope.PROTOTYPE,
                type,
                dependencyResolutionInput
        );
    }
}
