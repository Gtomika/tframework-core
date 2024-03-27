/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class CircularFieldDependencyTest {

    @InjectElement
    private CircularFieldDependencyTest selfInjectedElement;

    @Test
    public void shouldSelfInjectIntoField(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, DependencyResolutionException.class);
    }
}
