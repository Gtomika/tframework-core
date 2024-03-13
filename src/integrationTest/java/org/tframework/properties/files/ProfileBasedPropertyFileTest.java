/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
Because profile 'custom-profile' is set, the framework is expected to pick up the 'properties-custom-profile.yaml'
from the resources folder.
 */
@SetProfiles("custom-profile")
@IsolatedTFrameworkTest
public class ProfileBasedPropertyFileTest {

    @Test
    public void shouldReadPropertiesFromProfileBasedPropertyFile(
            @InjectProperty("integration-test.custom-profile.property") List<String> customProfileProperty
    ) {
        assertEquals(List.of("value1", "value2"), customProfileProperty);
    }

}
