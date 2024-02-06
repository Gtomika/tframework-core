/* Licensed under Apache-2.0 2024. */
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import org.junit.jupiter.api.Test;

class SystemPropertyReaderTest {

    @Test
    public void shouldReturnSystemProperty_whenItExists() {
        SystemPropertyReader reader = new SystemPropertyReader(name -> name+":value", null);
        String propertyValue = reader.readSystemProperty("test");
        assertEquals("test:value", propertyValue);
    }

    @Test
    public void shouldThrowException_whenSystemPropertyDoesNotExist() {
        SystemPropertyReader reader = new SystemPropertyReader(name -> null, null);
        var exception = assertThrows(SystemPropertyNotFoundException.class, () -> {
            reader.readSystemProperty("test");
        });
        assertEquals(
                exception.getMessageTemplate().formatted("test"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetAllSystemPropertyNames() {
        var expectedNames = Set.of("p1", "p2", "p3");
        SystemPropertyReader reader = new SystemPropertyReader(
                name -> name+":value",
                () -> expectedNames
        );

        var actualNames = reader.getAllSystemPropertyNames();
        assertEquals(expectedNames, actualNames);
    }

}
