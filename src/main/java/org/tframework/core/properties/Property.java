/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties;

/**
 * Represents a property, which consists of the name and the value.
 * @see PropertyValue
 */
public record Property(
        String name,
        PropertyValue value
) implements Comparable<Property> {

    @Override
    public int compareTo(Property o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name + ": " + value.toString();
    }
}
