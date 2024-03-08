/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.yamlparsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.readers.ReadersFactory;
import org.tframework.core.readers.ResourceFileReader;

@Slf4j
class SnakeYamlParserTest {

    private final SnakeYamlParser parser = YamlParsersFactory.createSnakeYamlParser();

    private final ResourceFileReader reader = ReadersFactory.createResourceFileReader();

    @Test
    public void shouldParseSimpleYaml() {
        String simpleYaml = reader.readResourceFile("parsers/simple.yaml");
        var parsedYaml = parser.parseYaml(simpleYaml);

        assertEquals("v1", parsedYaml.get("k1"));
        assertEquals(3, parsedYaml.get("k2"));
        assertEquals(4.1, parsedYaml.get("k3"));
        assertEquals(true, parsedYaml.get("k4"));
    }

    @Test
    public void shouldParseComplexYaml() {
        String complexYaml = reader.readResourceFile("parsers/complex.yaml");
        var parsedYaml = parser.parseYaml(complexYaml);

        assertEquals(List.of("v1-1", "v1-2", "v1-3"), parsedYaml.get("k1"));
        assertNull(parsedYaml.get("k2"));
        assertEquals(Map.of(
                "k3-1", "v3-1",
                "k3-2", "v3-2"
        ), parsedYaml.get("k3"));
    }

    @Test
    public void shouldThrowException_whenInvalidYaml() {
        String invalidYaml = reader.readResourceFile("parsers/invalid.yaml");
        var exception = assertThrows(YamlParsingException.class, () -> parser.parseYaml(invalidYaml));

        assertEquals(
                exception.getMessageTemplate().formatted(invalidYaml),
                exception.getMessage()
        );
    }

}
