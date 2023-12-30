/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.extractors.leaves.LeafExtractorsFactory;

/**
 * Utilities for creating {@link PropertiesExtractor}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyExtractorsFactory {

    /**
     * Creates a {@link PropertiesExtractor} which will be used during properties extraction.
     */
    public static PropertiesExtractor createPropertiesExtractor() {
        var leafExtractors = LeafExtractorsFactory.createLeafExtractors();
        return new RecursivePropertiesExtractor(leafExtractors);
    }

}
