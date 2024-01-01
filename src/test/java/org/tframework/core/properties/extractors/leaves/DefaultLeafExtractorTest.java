/* Licensed under Apache-2.0 2023. */
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
