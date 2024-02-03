/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PropertyParsersFactoryTest {

    @Test
    void shouldCreateDefaultPropertyParser() {
        var parser = PropertyParsersFactory.createDefaultPropertyParser();
        assertInstanceOf(DefaultPropertyParser.class, parser);
    }
}
