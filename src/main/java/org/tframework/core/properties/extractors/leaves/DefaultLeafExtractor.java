/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link LeafExtractor} implementation which handles all leaves that other
 * extractors could not, by simply converting them to a string.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultLeafExtractor implements LeafExtractor {

    @Override
    public PropertyValue extractLeaf(Object leaf) {
        return new SinglePropertyValue(String.valueOf(leaf));
    }

    @Override
    public boolean matchesLeaf(Object leaf) {
        return true;
    }
}
