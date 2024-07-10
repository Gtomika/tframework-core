/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
