/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.test.annotations.SetApplicationName;
import org.tframework.test.annotations.TFrameworkTest;

@SetApplicationName("fullApp")
@TFrameworkTest
public class TframeworkTestAnnotationTest {

    @Test
    //@Disabled("There are some elements all over the classpath that are picked up and are duplicates")
    public void shouldLaunchFullApplication(Application application) {
        assertEquals("fullApp", application.getName());
        assertEquals(DummyRootClass.class, application.getRootClass());

        assertTrue(application.getElementsContainer().hasElementContext(DummyRootClass.class));
    }

}
