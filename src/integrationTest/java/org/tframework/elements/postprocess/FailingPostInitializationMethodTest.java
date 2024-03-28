/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.postprocess;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class FailingPostInitializationMethodTest {

    @PostInitialization //this is a valid post init method, but it fails to execute
    public void afterInit() {
        throw new RuntimeException("I die");
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, RuntimeException.class);
    }

}
