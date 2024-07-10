/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.properties.converters;

import java.util.ArrayList;
import java.util.List;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} which can convert any {@link PropertyValue} into a {@link List} of strings.
 * Lists with other types are not supported. This converter will work for even single valued properties,
 * by create a collection with a single element. The returned list is mutable.
 */
@Element
public class StringListPropertyConverter implements PropertyConverter<List<String>> {

    private static final List<String> STRING_LIST = List.of();

    @Override
    public List<String> convert(PropertyValue propertyValue) {
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
