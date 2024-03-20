/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class AmbiguousDependencyTest {

    @Element(name = "string1")
    public String provideString1() {
        return "string1";
    }

    @Element(name = "string2")
    public String provideString2() {
        return "string2";
    }

    @Element
    public static class SomeElement {

        //it is not clear which String typed element we need
        public SomeElement(String dependency) {
            System.out.println(dependency);
        }
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, DependencyResolutionException.class);
    }

}
