/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when the framework detects that an element name is not unique.
 */
public class ElementNameNotUniqueException extends TFrameworkException {

    private static final String TEMPLATE = """
            Element name '%s' is not unique. Element names must be unique across the application.
            Please select a different name for one of the duplicate elements using '@Element(name = "yourName")'.
            - Existing element context: %s
            - Duplicate element context: %s""";

    public ElementNameNotUniqueException(ElementContext existingContext, ElementContext duplicateContext) {
        super(TEMPLATE.formatted(existingContext.getName(), existingContext, duplicateContext));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
