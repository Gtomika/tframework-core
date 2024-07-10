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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.tframework.core.properties.SinglePropertyValue;

class DefaultLeafExtractorTest {

    private final DefaultLeafExtractor extractor = new DefaultLeafExtractor();

    public static Stream<Arguments> provideLeafObjects() {
        return Stream.of(
                Arguments.of("leaf"),
                Arguments.of(1),
                Arguments.of(1.0),
                Arguments.of(true),
                Arguments.of('c')
        );
    }

    @ParameterizedTest
    @MethodSource("provideLeafObjects")
    void shouldExtractLeaf(Object leaf) {
        var propertyValue = extractor.extractLeaf(leaf);
        if(propertyValue instanceof SinglePropertyValue spv) {
            assertEquals(String.valueOf(leaf), spv.value());
        } else {
            fail("propertyValue is not a SinglePropertyValue");
        }
    }

    @ParameterizedTest
    @MethodSource("provideLeafObjects")
    void shouldMatchLeaf(Object leaf) {
        assertTrue(extractor.matchesLeaf(leaf));
    }

}
