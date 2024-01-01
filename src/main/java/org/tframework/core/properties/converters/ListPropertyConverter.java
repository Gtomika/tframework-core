/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import java.util.ArrayList;
import java.util.List;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} which can convert any {@link PropertyValue} into a {@link List} of strings.
 * Lists with other types are not supported. This converter will work for even single valued properties,
 * by create a collection with a single element. The returned list is mutable.
 */
public class ListPropertyConverter extends PropertyConverter<List<String>> {

    @Override
    protected List<String> convertInternal(PropertyValue propertyValue) {
        return switch (propertyValue) {
            case SinglePropertyValue spv -> {
                var list = new ArrayList<String>();
                list.add(spv.value()); //this is needed to allow null values
                yield list;
            }
            case ListPropertyValue lpv -> new ArrayList<>(lpv.values());
        };
    }

    @Override
    public Class<List<String>> getType() {
        return null;
    }
}
