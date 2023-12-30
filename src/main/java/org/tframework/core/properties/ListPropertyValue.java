/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.List;

public record ListPropertyValue(
        List<String> values
) implements PropertyValue {

    public ListPropertyValue {
        if (values == null) {
            throw new IllegalArgumentException("Values must not be null! Use SinglePropertyValue instead.");
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
