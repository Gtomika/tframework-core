/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.annotations.SetApplicationName;
import org.tframework.test.annotations.TFrameworkTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SetApplicationName("fullApp")
@TFrameworkTest
public class TframeworkTestAnnotationTest {

    @Test
    public void shouldLaunchFullApplication(@InjectElement Application application) {
        assertEquals("fullApp", application.getName());
        assertEquals(DummyRootClass.class, application.getRootClass());

        assertTrue(application.getElementsContainer().hasElementContext(DummyRootClass.class));
    }

}
