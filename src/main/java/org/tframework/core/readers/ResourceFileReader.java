/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Reader for fetching files from the resources.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) //for testing
public class ResourceFileReader {

    private final Function<String, String> resourceAccessor;

    /**
     * Reads the contents of the resource file into a string.
     * @param resourceName Name of the resource file.
     * @throws ResourceNotFoundException if resource with this name does not exist or could not be opened.
     */
    public String readResourceFile(String resourceName) {
        return Optional.ofNullable(resourceAccessor.apply(resourceName))
                .orElseThrow(() -> new ResourceNotFoundException(resourceName));
    }
}
