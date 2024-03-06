/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.filescanners.SystemPropertyFileScanner;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@Disabled("TODO: need a way to set system property before the framework starts, @BeforeAll is too late")
@IsolatedTFrameworkTest
public class CustomPropertyFileAsSystemPropertyTest {

    @BeforeAll
    public static void setUpClass() {
        System.setProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY, "custom-properties.yaml");
    }

    @Test
    public void shouldPickUpCustomPropertiesFile_fromSystemProperties(@InjectElement PropertiesContainer propertiesContainer) {
        TframeworkAssertions.assertHasPropertyWithValue(
                propertiesContainer,
                "integration-test.custom.property",
                "value"
        );
    }

    @AfterAll
    public static void tearDownClass() {
        System.clearProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY);
    }

}
