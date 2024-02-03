/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.yamlparsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class YamlParsersFactoryTest {

    @Test
    public void shouldCreateYamlParser() {
        var parser = YamlParsersFactory.createDefaultYamlParser();
        assertEquals(JacksonYamlParser.class, parser.getClass());
    }

}
