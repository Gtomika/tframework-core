/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class SpecialOptionalDependencyTest {

    //application element is present, so this optional will be populated
    @InjectElement
    private Optional<Application> applicationOptional;

    //no string elements, so this will be an empty optional
    @InjectElement
    private Optional<String> stringOptional;

    @Test
    public void shouldInjectSpecialOptionalElements(@InjectElement Application application) {
        assertTrue(applicationOptional.isPresent());
        assertEquals(application, applicationOptional.get());

        assertTrue(stringOptional.isEmpty());
    }

}
