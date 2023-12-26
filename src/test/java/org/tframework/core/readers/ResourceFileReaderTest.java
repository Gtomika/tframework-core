/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_CONTENT;
import static org.tframework.core.utils.ResourceTestUtils.TEST_RESOURCE_NAME;

import org.junit.jupiter.api.Test;

class ResourceFileReaderTest {

    @Test
    public void shouldReadResourceFile_ifAccessorFindsIt() {
        var resourceReader = new ResourceFileReader(name -> TEST_RESOURCE_CONTENT);

        var actualContent = resourceReader.readResourceFile(TEST_RESOURCE_NAME);
        assertEquals(TEST_RESOURCE_CONTENT, actualContent);
    }

    @Test
    public void shouldThrowResourceNotFoundException_whenAccessorDoesNotFindResource() {
        var resourceReader = new ResourceFileReader(name -> null);

        var exception = assertThrows(ResourceNotFoundException.class, () -> {
            resourceReader.readResourceFile(TEST_RESOURCE_NAME);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(TEST_RESOURCE_NAME),
                exception.getMessage()
        );
    }

}
