/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.extractors.leaves.LeafExtractorsFactory;
import org.tframework.core.utils.LogUtils;

/**
 * Utilities for creating {@link PropertiesExtractor}s.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyExtractorsFactory {

    /**
     * Creates a {@link PropertiesExtractor} which will be used during properties extraction.
     */
    public static PropertiesExtractor createPropertiesExtractor() {
        var leafExtractors = LeafExtractorsFactory.createLeafExtractors();
        var propertiesExtractor = new RecursivePropertiesExtractor(leafExtractors);

        log.debug("Created leaf extractors: '{}'", LogUtils.classNames(leafExtractors));
        log.debug("Created properties extractor: '{}'", propertiesExtractor.getClass().getName());

        return propertiesExtractor;
    }

}
