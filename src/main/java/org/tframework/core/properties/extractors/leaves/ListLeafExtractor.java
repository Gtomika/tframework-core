/* Licensed under Apache-2.0 2023. */
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
