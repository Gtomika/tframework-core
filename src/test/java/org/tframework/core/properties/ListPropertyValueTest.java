/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListPropertyValueTest {

    @Test
    public void shouldCreateListPropertyValues() {
        var lpv = new ListPropertyValue(List.of("a", "b", "c"));
        assertNotNull(lpv);
    }

    @Test
    public void shouldThrowExceptionWhenCreatingListPropertyValuesWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListPropertyValue(null));
    }

}
