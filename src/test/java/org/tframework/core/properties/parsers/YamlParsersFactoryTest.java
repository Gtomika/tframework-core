/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class YamlParsersFactoryTest {

    @Test
    public void shouldCreateJacksonYamlParser() {
        var parser = YamlParsersFactory.createJacksonYamlParser();
        assertNotNull(parser);
    }

}
