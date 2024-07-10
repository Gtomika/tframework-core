/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_CONTENT;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_NAME;

import org.junit.jupiter.api.Test;

class ReadersFactoryTest {

    @Test
    public void shouldCreateEnvironmentVariableReader() {
        var reader = ReadersFactory.createEnvironmentVariableReader();
        assertNotNull(reader);
    }

    @Test
    public void shouldCreateSystemPropertyReader() {
        var reader = ReadersFactory.createSystemPropertyReader();
        assertNotNull(reader);
    }

    @Test
    public void shouldCreateResourceFileReader() {
        var reader = ReadersFactory.createResourceFileReader();

        String actualContent = reader.readResourceFile(TEST_RESOURCE_NAME);
        assertEquals(TEST_RESOURCE_CONTENT, actualContent);
    }

}
