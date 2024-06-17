/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.scanning;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementNameNotUniqueException;
import org.tframework.core.elements.annotations.Element;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class DuplicateElementTest {

    //both of these element will have the same name, which is not allowed

    @Element
    public String provideString1() {
        return "string1";
    }

    @Element
    public String provideString2() {
        return "string2";
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, ElementNameNotUniqueException.class);
    }

}
