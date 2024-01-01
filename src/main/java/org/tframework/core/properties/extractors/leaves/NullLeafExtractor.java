/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link LeafExtractor} implementation which handles {@code null} leaves.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class NullLeafExtractor implements LeafExtractor {

    @Override
    public PropertyValue extractLeaf(Object leaf) {
        return new SinglePropertyValue(null);
    }

    @Override
    public boolean matchesLeaf(Object leaf) {
        return leaf == null;
    }
}
