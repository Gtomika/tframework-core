/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SnakeYamlParserFactoryTest {

    @Test
    public void shouldCreateSnakeYamlParser_whenSnakeYamlIsOnClasspath() {
        var parser = YamlParsersFactory.createAvailableYamlParser();
        assertEquals(SnakeYamlParser.class, parser.getClass());
    }

}
