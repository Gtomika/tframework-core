package org.tframework.test;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;
import org.tframework.test.annotations.SingleClassTFrameworkTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SingleClassTFrameworkTest
public class SingleClassTFrameworkAnnotationTest {

    @Test
    public void shouldLaunchSingleClassTFrameworkApplication(Application application) {
        assertEquals(SingleClassTFrameworkAnnotationTest.class, application.getRootClass());
        assertTrue(application.getElementsContainer().hasElementContext(SomeElement.class));
    }

    @Element
    public static class SomeElement {}

}
