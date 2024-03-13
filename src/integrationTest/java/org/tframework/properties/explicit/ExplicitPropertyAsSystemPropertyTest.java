/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.explicit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.commons.annotations.BeforeFrameworkInitialization;
import org.tframework.test.commons.utils.SystemPropertyHelper;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@BeforeFrameworkInitialization(callback = ExplicitPropertyAsSystemPropertyTest.SystemPropertySetter.class)
@IsolatedTFrameworkTest
public class ExplicitPropertyAsSystemPropertyTest {

    private static final SystemPropertyHelper systemPropertyHelper = new SystemPropertyHelper();

    public static class SystemPropertySetter implements Runnable {

        @Override
        public void run() {
            systemPropertyHelper.setFrameworkPropertyIntoSystemProperties(
                    "integration-test.custom.property",
                    "true"
            );
        }
    }

    @Test
    public void shouldPickUpExplicitProperty_fromSystemProperties(
            @InjectProperty("integration-test.custom.property") boolean customProperty
    ) {
        assertTrue(customProperty);
    }

    @AfterAll
    public static void tearDownClass() {
        systemPropertyHelper.cleanUp();
    }

}
