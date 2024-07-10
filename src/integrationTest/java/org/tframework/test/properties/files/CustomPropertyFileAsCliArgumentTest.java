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
package org.tframework.test.properties.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.filescanners.CliArgumentPropertyFileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments(
        CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR + "custom-properties.yaml"
)
@IsolatedTFrameworkTest
public class CustomPropertyFileAsCliArgumentTest {

    @Test
    public void shouldPickUpCustomPropertiesFile_fromCommandLineArguments(
            @InjectProperty("integration-test.custom.property") String customProperty
    ) {
        assertEquals("value", customProperty);
    }

}
