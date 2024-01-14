/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class ConstructorFiltersFactoryTest {

    @Test
    void shouldCreateDefaultConstructorFilter() {
        ConstructorFilter constructorFilter = ConstructorFiltersFactory.createDefaultConstructorFilter();
        assertInstanceOf(SimpleConstructorFilter.class, constructorFilter);
    }
}
