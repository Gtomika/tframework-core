/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
Because profile 'custom-profile' is set, the framework is expected to pick up the 'properties-custom-profile.yaml'
from the resources folder.
 */
@SetProfiles("custom-profile")
@IsolatedTFrameworkTest
public class ProfileBasedPropertyFileTest {

    @Test
    public void shouldReadPropertiesFromProfileBasedPropertyFile(@InjectElement PropertiesContainer propertiesContainer) {
        TframeworkAssertions.assertHasPropertyWithValue(
                propertiesContainer,
                "integration-test.custom-profile.property",
                "value"
        );
    }

}
