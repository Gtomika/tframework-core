/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.List;

public record ListPropertyValue(
        List<String> values
) implements PropertyValue {
}
