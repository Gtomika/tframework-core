/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties;

import java.util.List;
import lombok.NonNull;

/**
 * Creates instances of the {@link PropertiesContainer}.
 */
public class PropertiesContainerFactory {

    /**
     * Creates a {@link PropertiesContainer} from the given list of properties.
     * @param properties Properties list to create the container from, cannot be null.
     */
    public static PropertiesContainer fromProperties(@NonNull List<Property> properties) {
        return new PropertiesContainer(properties);
    }

    /**
     * Creates an empty {@link PropertiesContainer}.
     */
    public static PropertiesContainer empty() {
        return fromProperties(List.of());
    }

}
