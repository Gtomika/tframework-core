/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class PropertyExtractorsFactoryTest {

    @Test
    void shouldCreatePropertiesExtractor() {
        PropertiesExtractor propertiesExtractor = PropertyExtractorsFactory.createPropertiesExtractor();
        assertInstanceOf(RecursivePropertiesExtractor.class, propertiesExtractor);
    }

}
