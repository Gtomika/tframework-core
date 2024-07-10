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
