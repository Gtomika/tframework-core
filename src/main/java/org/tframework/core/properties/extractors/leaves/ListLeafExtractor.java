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
package org.tframework.core.properties.extractors.leaves;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;

/**
 * A {@link LeafExtractor} implementation which handles leaves that are lists. Regardless of the
 * types in the leaf list, the extracted {@link PropertyValue} will be a {@link ListPropertyValue} with string values.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ListLeafExtractor implements LeafExtractor {

    @Override
    public PropertyValue extractLeaf(Object leaf) {
        List<?> list = (List<?>) leaf;
        List<String> strings = list.stream()
                .map(object -> object == null ? null : String.valueOf(object))
                .toList();
        return new ListPropertyValue(strings);
    }

    @Override
    public boolean matchesLeaf(Object leaf) {
        return leaf != null && List.class.isAssignableFrom(leaf.getClass());
    }
}
