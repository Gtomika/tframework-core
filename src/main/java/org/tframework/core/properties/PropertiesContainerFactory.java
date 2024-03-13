/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties;

import java.util.List;
import lombok.NonNull;
import org.tframework.core.properties.converters.PropertyConverterAggregator;
import org.tframework.core.properties.converters.PropertyConvertersFactory;

/**
 * Creates instances of the {@link PropertiesContainer}.
 */
public class PropertiesContainerFactory {

    /**
     * Creates a {@link PropertiesContainer} from the given list of properties.
     * @param properties Properties list to create the container from, cannot be null.
     */
    public static PropertiesContainer fromProperties(@NonNull List<Property> properties) {
        var converters = PropertyConvertersFactory.getAvailableConverters();
        var aggregator = PropertyConverterAggregator.usingConverters(converters);
        return new PropertiesContainer(properties, aggregator);
    }

    /**
     * Creates an empty {@link PropertiesContainer}.
     */
    public static PropertiesContainer empty() {
        return fromProperties(List.of());
    }

    static PropertiesContainer fromProperties(@NonNull List<Property> properties, PropertyConverterAggregator aggregator) {
        return new PropertiesContainer(properties, aggregator);
    }

}
