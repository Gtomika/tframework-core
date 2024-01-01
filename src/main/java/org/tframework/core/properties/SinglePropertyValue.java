/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

public record SinglePropertyValue(
        String value
) implements PropertyValue {

    @Override
    public String toString() {
        return value;
    }
}
