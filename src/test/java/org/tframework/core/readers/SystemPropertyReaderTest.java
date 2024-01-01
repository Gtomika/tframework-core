/* Licensed under Apache-2.0 2024. */
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SystemPropertyReaderTest {

    @Test
    public void shouldReturnSystemProperty_whenItExists() {
        SystemPropertyReader reader = new SystemPropertyReader(name -> name+":value");
        String propertyValue = reader.readProperty("test");
        assertEquals("test:value", propertyValue);
    }

    @Test
    public void shouldThrowException_whenSystemPropertyDoesNotExist() {
        SystemPropertyReader reader = new SystemPropertyReader(name -> null);
        var exception = assertThrows(SystemPropertyNotFoundException.class, () -> {
            reader.readProperty("test");
        });
        assertEquals(
                exception.getMessageTemplate().formatted("test"),
                exception.getMessage()
        );
    }

}
