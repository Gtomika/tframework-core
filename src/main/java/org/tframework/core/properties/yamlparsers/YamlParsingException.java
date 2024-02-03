/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.yamlparsers;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when the provided content that should be valid YAML cannot be parsed.
 */
public class YamlParsingException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to parse YAML:\n%s";

    public YamlParsingException(String yaml, Throwable cause) {
        super(TEMPLATE.formatted(yaml), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
