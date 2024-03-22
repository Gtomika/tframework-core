/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * A special {@link ElementSource} for pre-constructed elements.
 */
public record PreConstructedElementSource(
        Object preConstructedInstance
) implements ElementSource {

    @Override
    public AnnotatedElement annotatedSource() {
        return preConstructedInstance.getClass();
    }

    @Override
    public List<Parameter> elementConstructionParameters() {
        //this information is unknown, because this element is constructed outside the framework
        return List.of();
    }
}
