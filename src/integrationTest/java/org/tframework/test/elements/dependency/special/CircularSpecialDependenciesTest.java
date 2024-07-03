/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.dependency.special;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class CircularSpecialDependenciesTest {

    @InjectElement
    private List<CircularSpecialDependenciesTest> selfList;

    @Test
    public void shouldFailInitializationWhenCircularDependencyDetectedInSpecialDependencies(
            @InjectInitializationException Exception exception
    ) {
        assertInitializationExceptionWithCause(exception, DependencyResolutionException.class);
    }

}
