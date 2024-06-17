/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.dependency;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class CircularDependencyTest {

    @Element
    public static class SomeElement {
        public SomeElement(OtherElement otherElement) {}
    }

    @Element
    public static class OtherElement {
        public OtherElement(SomeElement someElement) {}
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        //there is also CircularDependencyException in the cause chain, but the ROOT cause is DependencyResolutionException
        assertInitializationExceptionWithCause(e, DependencyResolutionException.class);
    }
}
