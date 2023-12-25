/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ResourceFileReaderTest {

    private static final String RESOURCE_NAME = "text.txt";

    @Test
    public void shouldReadResourceFile_ifAccessorFindsIt() {
        String expectedContent = "this is a test";
        var resourceReader = new ResourceFileReader(name -> expectedContent);

        var actualContent = resourceReader.readResourceFile(RESOURCE_NAME);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void shouldThrowResourceNotFoundException_whenAccessorDoesNotFindResource() {
        var resourceReader = new ResourceFileReader(name -> null);

        var exception = assertThrows(ResourceNotFoundException.class, () -> {
            resourceReader.readResourceFile(RESOURCE_NAME);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(RESOURCE_NAME),
                exception.getMessage()
        );
    }

}
