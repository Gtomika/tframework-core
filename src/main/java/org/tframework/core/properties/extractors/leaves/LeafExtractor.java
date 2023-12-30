/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import org.tframework.core.properties.PropertyValue;

/**
 * The node extractor transforms a YAML leaf into a {@link PropertyValue}. A leaf is anything
 * that is not a {@link java.util.Map} of values. For example in the YAML
 * <pre>{@code
 * p1: v1
 * p2: v2
 * p3:
 *   p3-1: v3-1
 *   p3-2: v3-2
 * }</pre>
 * the leaves are {@code p1, p2, p3-1, p3-2}, but not {@code p3}, because that is a map of values itself.
 * @see LeafExtractorsFactory
 */
public interface LeafExtractor {

    /**
     * Extracts the given leaf into a {@link PropertyValue}.
     * @param leaf The leaf to extract: it must match this extractor, that is, {@link #matchesLeaf(Object)} must return {@code true}.
     */
    PropertyValue extractLeaf(Object leaf);

    /**
     * Returns {@code true} if this extractor matches the given leaf and can convert it to a {@link PropertyValue}.
     * @param leaf The leaf to check, which can also be null.
     */
    boolean matchesLeaf(Object leaf);

}
