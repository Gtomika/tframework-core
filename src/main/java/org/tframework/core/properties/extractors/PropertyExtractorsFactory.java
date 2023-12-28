/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyExtractorsFactory {

    public static PropertiesExtractor createPropertiesExtractor() {
        return new RecursivePropertiesExtractor();
    }

}
