/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} which can convert any {@link PropertyValue} into a {@link List} of strings.
 * Lists with other types are not supported. This converter will work for even single valued properties,
 * by create a collection with a single element. The returned list is mutable.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ListPropertyConverter extends PropertyConverter<List<String>> {

    private static final List<String> STRING_LIST = List.of();

    @Override
    protected List<String> convertInternal(PropertyValue propertyValue) {
        return switch (propertyValue) {
            case SinglePropertyValue(var value) -> {
                var list = new ArrayList<String>();
                list.add(value); //this is needed to allow null values
                yield list;
            }
            case ListPropertyValue(var values) -> new ArrayList<>(values);
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<String>> getType() {
        return (Class<List<String>>) STRING_LIST.getClass();
    }
}
