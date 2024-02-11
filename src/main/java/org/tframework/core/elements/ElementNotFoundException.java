/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when an element is requested, but it is not found.
 */
public class ElementNotFoundException extends TFrameworkException {

    static final String HAS_NAME = "has name";
    static final String ASSIGNABLE_TO_TYPE = "is assignable to type";

    private static final String TEMPLATE = "Element not found which %s: '%s'";

    public ElementNotFoundException(String name) {
        super(TEMPLATE.formatted(HAS_NAME, name));
    }

    public ElementNotFoundException(Class<?> elementType) {
        super(TEMPLATE.formatted(ASSIGNABLE_TO_TYPE, elementType.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
