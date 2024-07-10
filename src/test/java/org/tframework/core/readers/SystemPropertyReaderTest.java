/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
        var exception = assertThrows(SystemPropertyNotFoundException.class, () ->
                reader.readSystemProperty("test"));
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
