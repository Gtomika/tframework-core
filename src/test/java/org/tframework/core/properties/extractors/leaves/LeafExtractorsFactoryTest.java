/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LeafExtractorsFactoryTest {

    @Test
    void shouldCreateLeafExtractors_inCorrectOrder() {
        var leafExtractors = LeafExtractorsFactory.createLeafExtractors();
        assertEquals(3, leafExtractors.size());
        assertInstanceOf(NullLeafExtractor.class, leafExtractors.get(0));
        assertInstanceOf(ListLeafExtractor.class, leafExtractors.get(1));
        assertInstanceOf(DefaultLeafExtractor.class, leafExtractors.get(2));
    }
}
