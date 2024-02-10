/* Licensed under Apache-2.0 2024. */
package org.tframework.core;

import java.util.List;
import java.util.stream.Collectors;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when an element is requested by type, however there
 * are multiple choices of elements that can be selected.
 */
public class AmbiguousElementTypeException extends TFrameworkException {

    private static final String TEMPLATE = """
            There are multiple elements that are assignable to the type: '%s'
            %s""";

    public AmbiguousElementTypeException(Class<?> ambiguousType, List<ElementContext> assignableElements) {
        super(TEMPLATE.formatted(
                ambiguousType.getName(),
                assignableElements.stream()
                        .map(element -> "- " + element.toString())
                        .collect(Collectors.joining("\n"))
        ));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
