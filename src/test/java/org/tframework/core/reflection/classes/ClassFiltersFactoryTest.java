/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ClassFiltersFactoryTest {

    @Test
    public void shouldCreateDefaultClassFilter() {
        var filter = ClassFiltersFactory.createDefaultClassFilter();
        assertNotNull(filter);
    }

}
