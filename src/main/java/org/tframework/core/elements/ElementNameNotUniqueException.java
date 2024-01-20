/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when the framework detects that an element name is not unique.
 */
public class ElementNameNotUniqueException extends TFrameworkException {

    private static final String TEMPLATE = """
            Element name '%s' is not unique. Element names must be unique across the application.
            Please select a different name for one of the duplicate elements using '@Element(name = "yourName")'.""";

    public ElementNameNotUniqueException(String elementName) {
        super(TEMPLATE.formatted(elementName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
