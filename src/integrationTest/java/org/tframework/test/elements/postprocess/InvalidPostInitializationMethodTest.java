/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.postprocess;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.postprocessing.PostInitializationMethodException;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class InvalidPostInitializationMethodTest {

    @PostInitialization //invalid, because cannot have parameters and cant be static
    public static void afterInit(int x) {}

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, PostInitializationMethodException.class);
    }
}
