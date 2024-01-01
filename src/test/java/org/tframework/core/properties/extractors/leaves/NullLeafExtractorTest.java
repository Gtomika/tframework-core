/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.SinglePropertyValue;

class NullLeafExtractorTest {

    private final NullLeafExtractor extractor = new NullLeafExtractor();

    @Test
    void shouldExtractLeaf() {
        var propertyValue = extractor.extractLeaf(null);
        if(propertyValue instanceof SinglePropertyValue spv) {
            assertNull(spv.value());
        } else {
            fail("propertyValue is not a SinglePropertyValue");
        }
    }

    @Test
    void shouldMatchLeaf_ifNull() {
        assertTrue(extractor.matchesLeaf(null));
    }

    @Test
    void shouldNotMatchLeaf_ifNotNull() {
        assertFalse(extractor.matchesLeaf("not null"));
    }
}
