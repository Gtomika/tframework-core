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
package org.tframework.core.properties.filescanners;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;

/**
 *  A {@link PropertyFileScanner} implementation that scans for the property files specified as system properties.
 *  The system property checked is {@value #PROPERTY_FILES_SYSTEM_PROPERTY}. If not provided, this scanner will not add
 *  any property files. Multiple property files can be provided with comma separation. For example, setting
 *  this system property to {@code custom-properties.yaml,another-properties.yaml} will make this scanner detect
 *  the files {@code custom-properties.yaml} and {@code another-properties.yaml} in the {@code resources} folder.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemPropertyFileScanner implements PropertyFileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String PROPERTY_FILES_SYSTEM_PROPERTY = "tframework.propertyFiles";

    private final SystemPropertyReader systemPropertyReader;

    @Override
    public List<String> scan() {
        try {
            return Arrays.stream(systemPropertyReader.readSystemProperty(PROPERTY_FILES_SYSTEM_PROPERTY).split(","))
                    .map(String::trim)
                    .peek(p -> log.debug("Adding property file '{}' as specified by the '{}' system property.", p, PROPERTY_FILES_SYSTEM_PROPERTY))
                    .toList();
        } catch (SystemPropertyNotFoundException e) {
            log.debug("The '{}' system property was not found, so no property files will be added by this scanner.", PROPERTY_FILES_SYSTEM_PROPERTY);
            return List.of();
        }
    }

    @Override
    public String sourceName() {
        return "System Property (named " + PROPERTY_FILES_SYSTEM_PROPERTY + ")";
    }
}
