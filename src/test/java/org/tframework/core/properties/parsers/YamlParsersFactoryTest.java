/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class YamlParsersFactoryTest {

    @Test
    public void shouldCreateJacksonYamlParser_whenJacksonYamlIsOnClasspath() {
        var parser = YamlParsersFactory.createAvailableYamlParser();
        assertEquals(JacksonYamlParser.class, parser.getClass());
    }

}
