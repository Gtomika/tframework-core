/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import org.tframework.core.TFrameworkException;

/**
 * Exception thrown when some step of the property parsing process failed.
 */
public class PropertyParsingException extends TFrameworkException {

    private static final String TEMPLATE = """
            Failed to parse raw property value!
            - Raw property: %s
            - Reason: %s
            """;

    public PropertyParsingException(String rawProperty, String reason) {
        super(TEMPLATE.formatted(rawProperty, reason));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
