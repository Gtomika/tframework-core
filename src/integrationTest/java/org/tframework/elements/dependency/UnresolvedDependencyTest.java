/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class UnresolvedDependencyTest {

    @Element
    public static class SomeElement {

        //there is no 'java.io.File' typed element in this app
        public SomeElement(File fileElement) {
            System.out.println(fileElement.getName());
        }
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        var resolutionException = assertInitializationExceptionWithCause(e, DependencyResolutionException.class);
        assertEquals(File.class, resolutionException.getDependencyType());
    }
}
