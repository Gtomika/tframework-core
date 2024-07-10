/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.test.properties;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.filescanners.CliArgumentPropertyFileScanner;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments(CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY + "=custom-properties.yaml")
@SetProfiles("custom-profile")
@SetProperties({
        "integration-test.explicit.property=value",
        "integration-test.explicit.list-property=[value1,value2]"
})
@IsolatedTFrameworkTest
public class MultipleSourcesPropertiesTest {

    @Test
    public void shouldFindPropertiesFromMultipleSources(@InjectElement PropertiesContainer propertiesContainer) {
        //from the default properties file
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.default.property", "1");

        //from the properties file activated by profile
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.custom-profile.property", List.of("value1", "value2"));

        //from the custom properties file
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.custom.property", "value");
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.custom.list-property", List.of("value1", "value2"));

        //explicitly specified properties
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.explicit.property", "value");
        TframeworkAssertions.assertHasPropertyWithValue(propertiesContainer, "integration-test.explicit.list-property", List.of("value1", "value2"));
    }

}
