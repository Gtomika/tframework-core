/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import org.tframework.core.properties.PropertyValue;

/**
 * Result of the property parsing, contains the name and value of a property.
 */
public record ParsedProperty(
        String name,
        PropertyValue value
) {
}
