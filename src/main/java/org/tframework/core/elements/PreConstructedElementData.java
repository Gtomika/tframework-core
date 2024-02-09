/* Licensed under Apache-2.0 2024. */
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
