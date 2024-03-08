/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.explicit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.test.commons.annotations.BeforeFrameworkInitialization;
import org.tframework.test.commons.utils.SystemPropertyHelper;
import org.tframework.test.commons.utils.TframeworkAssertions;
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
                    "value"
            );
        }
    }

    @Test
    public void shouldPickUpExplicitProperty_fromSystemProperties(@InjectElement PropertiesContainer propertiesContainer) {
        TframeworkAssertions.assertHasPropertyWithValue(
                propertiesContainer,
                "integration-test.custom.property",
                "value"
        );
    }

    @AfterAll
    public static void tearDownClass() {
        systemPropertyHelper.cleanUp();
    }

}
