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
package org.tframework.core.elements;

import lombok.Builder;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;

/**
 * Contains data about a {@link org.tframework.core.elements.annotations.PreConstructedElement}.
 * @param name Name of the element. Use {@link #from(Object)} if this should be the default value.
 * @param preConstructedInstance The instance the element should use.
 * @param overrideExistingElement If this is true, an existing element of the same name will be overridden by
 *                                this one. If this is false, and there is already an element with this name,
 *                                a {@link ElementNameNotUniqueException} will be thrown.
 */
@Builder
public record PreConstructedElementData(
        String name,
        Object preConstructedInstance,
        boolean overrideExistingElement
) {

    /**
     * Creates pre-constructed element data from an instance which will have default name, based on its type.
     */
    public static PreConstructedElementData from(@NonNull Object preConstructedInstance) {
        return new PreConstructedElementData(Element.NAME_NOT_SPECIFIED, preConstructedInstance, false);
    }

}
