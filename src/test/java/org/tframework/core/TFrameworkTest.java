/* Licensed under Apache-2.0 2024. */
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class TFrameworkTest {

    @Test
    public void shouldInitializeFramework() {
        String[] args = new String[] {
                "tframework.profiles=unit-test,demo",
                "tframework.propertyFiles=properties/custom-properties.yaml"
        };
        var application = TFramework.start(args);

        assertTrue(application.getProfilesContainer().isProfileSet("default"));
        assertTrue(application.getProfilesContainer().isProfileSet("unit-test"));
        assertTrue(application.getProfilesContainer().isProfileSet("demo"));

        assertEquals("test", application.getPropertiesContainer().getPropertyValue("custom.string-prop"));
        assertEquals("1", application.getPropertiesContainer().getPropertyValue("custom.int-prop"));
        assertEquals("true", application.getPropertiesContainer().getPropertyValue("custom.boolean-prop"));
        assertEquals(
                List.of("test1", "test2", "test3"),
                application.getPropertiesContainer().getPropertyValueList("custom.list-prop")
        );
    }

}
