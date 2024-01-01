/* Licensed under Apache-2.0 2023. */
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
    public void shouldCreateResourceFileReader() {
        var reader = ReadersFactory.createResourceFileReader();

        String actualContent = reader.readResourceFile(TEST_RESOURCE_NAME);
        assertEquals(TEST_RESOURCE_CONTENT, actualContent);
    }

}
