/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

/**
 * Data class for a raw property name-value pair that is already separated into the
 * name and value parts.
 */
public record SeparatedProperty(
        String name,
        String value
) {
}
