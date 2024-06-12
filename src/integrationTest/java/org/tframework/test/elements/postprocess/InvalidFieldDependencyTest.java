/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.postprocess;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.postprocessing.FieldInjectionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class InvalidFieldDependencyTest {

    @InjectElement
    private static Application application;

    @Test
    public void shouldFailInitialization_becauseStaticFieldCannotBeInjected(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, FieldInjectionException.class);
    }

}
