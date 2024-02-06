/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PropertyParsingUtilsTest {

    @Test
    public void shouldSeparateSimpleProperty() {
        String raw = "test.prop=123";
        var separated = PropertyParsingUtils.separateNameValue(raw);
        assertEquals("test.prop", separated.name());
        assertEquals("123", separated.value());
    }

    @Test
    public void shouldSeparatePropertyWithEscapedSeparators() {
        String raw = "test\\=prop=123\\=456";
        var separated = PropertyParsingUtils.separateNameValue(raw);
        assertEquals("test\\=prop", separated.name());
        assertEquals("123\\=456", separated.value());
    }

    @Test
    public void shouldThrowPropertyParsingException_whenPropertyContainsNoSeparators() {
        String raw = "test.prop: 123";
        var exception = assertThrows(PropertyParsingException.class, () -> PropertyParsingUtils.separateNameValue(raw));

        assertEquals(
                exception.getMessageTemplate().formatted(raw, PropertyParsingUtils.SEPARATOR_NOT_FOUND_ERROR),
                exception.getMessage()
        );
    }

    @Test
    public void shouldThrowPropertyParsingException_whenPropertyContainsMultipleSeparators() {
        String raw = "test=prop=123";
        var exception = assertThrows(PropertyParsingException.class, () -> PropertyParsingUtils.separateNameValue(raw));

        assertEquals(
                exception.getMessageTemplate().formatted(raw, PropertyParsingUtils.MULTIPLE_SEPARATOR_FOUND_ERROR),
                exception.getMessage()
        );
    }

    public static Stream<Arguments> createIsListValueInputs() {
        return Stream.of(
                Arguments.of("[1,2,3]", true),
                Arguments.of("[]", true),
                Arguments.of("", false),
                Arguments.of("[abc", false),
                Arguments.of("abc]", false),
                Arguments.of("\\[1,2,3]", false),
                Arguments.of("[1,2,3\\]", false),
                Arguments.of("\\[1,2,3\\]", false)
        );
    }

    @ParameterizedTest
    @MethodSource("createIsListValueInputs")
    public void shouldDetectIfValueIsList(String value, boolean expectedResult) {
        assertEquals(expectedResult, PropertyParsingUtils.isListValue(value));
    }

    public static Stream<Arguments> createExtractListElementsInputs() {
        return Stream.of(
                Arguments.of("[1,2,3]", List.of("1", "2", "3")),
                Arguments.of("[]", List.of()),
                Arguments.of("[abc]", List.of("abc")),
                Arguments.of("[a,b\\,c]", List.of("a", "b,c")),
                Arguments.of("[,]", List.of("", ""))
        );
    }

    @ParameterizedTest
    @MethodSource("createExtractListElementsInputs")
    public void shouldExtractListElements(String rawValue, List<String> expectedElements) {
        var actualElements = PropertyParsingUtils.extractListElements(rawValue);
        assertEquals(expectedElements, actualElements);
    }

}
