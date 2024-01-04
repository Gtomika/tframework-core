/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when an element is requested by name, but it is not found.
 */
public class ElementNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "Element not found with name '%s'";

    public ElementNotFoundException(String name) {
        super(String.format(TEMPLATE, name));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
