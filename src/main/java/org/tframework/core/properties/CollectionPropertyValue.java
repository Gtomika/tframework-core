/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.List;

public record CollectionPropertyValue(
        List<String> values
) implements PropertyValue {
}
