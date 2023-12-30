/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;

class ListLeafExtractorTest {

    private final ListLeafExtractor extractor = new ListLeafExtractor();

    @Test
    public void shouldExtractLeaf_ifStringList() {
        List<String> leaf = List.of("a", "b", "c");
        var propertyValue = extractor.extractLeaf(leaf);
        if(propertyValue instanceof ListPropertyValue lpv) {
            assertEquals(leaf, lpv.values());
        } else {
            fail("propertyValue is not a ListPropertyValue");
        }
    }

    @Test
    public void shouldExtractLeaf_ifOtherList() {
        List<Integer> leaf = new ArrayList<>();
        leaf.add(1);
        leaf.add(2);
        leaf.add(null);

        var propertyValue = extractor.extractLeaf(leaf);
        if(propertyValue instanceof ListPropertyValue lpv) {
            List<String> expected = new ArrayList<>();
            expected.add("1");
            expected.add("2");
            expected.add(null);
            assertEquals(expected, lpv.values());
        } else {
            fail("propertyValue is not a ListPropertyValue");
        }
    }

    @Test
    public void shouldMatchLeaf_ifList() {
        List<String> leaf = List.of("a", "b", "c");
        assertTrue(extractor.matchesLeaf(leaf));
    }

    @Test
    public void shouldNotMatchLeaf_ifNotList() {
        String leaf = "leaf";
        assertFalse(extractor.matchesLeaf(leaf));
    }

    @Test
    public void shouldNotMatchLeaf_ifNull() {
        assertFalse(extractor.matchesLeaf(null));
    }

}
