/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.filescanners.SystemPropertyFileScanner;
import org.tframework.test.commons.annotations.BeforeFrameworkInitialization;
import org.tframework.test.commons.utils.SystemPropertyHelper;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@BeforeFrameworkInitialization(callback = CustomPropertyFileAsSystemPropertyTest.SystemPropertyCallback.class)
@IsolatedTFrameworkTest
public class CustomPropertyFileAsSystemPropertyTest {

    private static final SystemPropertyHelper systemPropertyHelper = new SystemPropertyHelper();

    public static class SystemPropertyCallback implements Runnable {

        public SystemPropertyCallback() {}

        @Override
        public void run() {
            systemPropertyHelper.setIntoSystemProperties(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY, "custom-properties.yaml");
        }
    }

    @Test
    public void shouldPickUpCustomPropertiesFile_fromSystemProperties(
            @InjectProperty("integration-test.custom.property") String customProperty
    ) {
        assertEquals("value", customProperty);
    }

    @AfterAll
    public static void tearDownClass() {
        systemPropertyHelper.cleanUp();
    }

}
