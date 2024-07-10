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
package org.tframework.test.properties.explicit;

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
