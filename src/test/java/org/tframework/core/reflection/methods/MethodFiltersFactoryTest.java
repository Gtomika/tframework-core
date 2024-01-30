/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MethodFiltersFactoryTest {

    @Test
    void shouldCreateDefaultMethodFilter() {
        MethodFilter methodFilter = MethodFiltersFactory.createDefaultMethodFilter();
        assertInstanceOf(SimpleMethodFilter.class, methodFilter);
    }
}
