/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JacksonYamlParserFactoryTest {

    @Test
    public void shouldCreateJacksonYamlParser_whenJacksonYamlIsOnClasspath() {
        var parser = YamlParsersFactory.createAvailableYamlParser();
        assertEquals(JacksonYamlParser.class, parser.getClass());
    }

}
