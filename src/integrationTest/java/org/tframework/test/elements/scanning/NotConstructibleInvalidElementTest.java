/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.scanning;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.assembler.ElementContextAssemblingException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class NotConstructibleInvalidElementTest {

    @Element
    public static class SomeElement {

        //private constructor cannot be used to create an element instance
        private SomeElement() {}
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        var constructingException = assertInitializationExceptionWithCause(e, ElementContextAssemblingException.class);
        assertTrue(constructingException.getMessage().contains(SomeElement.class.getName()));
    }

}
