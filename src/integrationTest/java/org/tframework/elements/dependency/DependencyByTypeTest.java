/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class DependencyByTypeTest {

    public interface SomeInterface {}

    @Element
    public static class SomeElement implements SomeInterface {}

    @Test
    public void shouldInjectDependencyBasedOnType(@InjectElement SomeInterface someInterface) {
        assertInstanceOf(SomeElement.class, someInterface);
    }
}
