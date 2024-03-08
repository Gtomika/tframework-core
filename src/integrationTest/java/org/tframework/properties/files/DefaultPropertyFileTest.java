/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
The framework is expected to pick up the default property file 'properties.yaml' from the
resources folder.
 */
@IsolatedTFrameworkTest
public class DefaultPropertyFileTest {

    @Test
    public void shouldReadPropertiesFromDefaultPropertyFile(@InjectElement PropertiesContainer propertiesContainer) {
        TframeworkAssertions.assertHasPropertyWithValue(
                propertiesContainer,
                "integration-test.default.property",
                "value" //value is specified in 'properties.yaml'
        );
    }
}
