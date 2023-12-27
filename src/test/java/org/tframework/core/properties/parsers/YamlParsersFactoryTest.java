/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class YamlParsersFactoryTest {

    @Test
    public void shouldThrowException_whenNoYamlParsingLibraryFoundOnClasspath() {
        var exception = assertThrows(NoYamlParserLibraryException.class, YamlParsersFactory::createAvailableYamlParser);

        assertEquals(
                exception.getMessageTemplate().formatted(),
                exception.getMessage()
        );

        log.info(exception.getMessage());
    }

}
