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
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

/**
 * A {@link PropertyFileScanner} that finds property files from the environment variable
 * {@value PROPERTY_FILES_ENVIRONMENT_VARIABLE}. This variable can contain a comma separated list
 * of property files. If the variable is not specified, this scanner will not add any files.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvironmentPropertyFileScanner implements PropertyFileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String PROPERTY_FILES_ENVIRONMENT_VARIABLE = "TFRAMEWORK_PROPERTY_FILES";

    private final EnvironmentVariableReader environmentPropertyReader;

    @Override
    public List<String> scan() {
        try {
            return Arrays.stream(environmentPropertyReader.readVariable(PROPERTY_FILES_ENVIRONMENT_VARIABLE).split(","))
                    .map(String::trim)
                    .peek(p -> log.debug("Adding property file '{}' as specified by the '{}' environment variable.", p, PROPERTY_FILES_ENVIRONMENT_VARIABLE))
                    .toList();
        } catch (EnvironmentVariableNotFoundException e) {
            log.debug("The '{}' environment variable was not found, so no property files will be added by this scanner.",
                    PROPERTY_FILES_ENVIRONMENT_VARIABLE);
            return List.of();
        }
    }

    @Override
    public String sourceName() {
        return "Environment Variable (named " + PROPERTY_FILES_ENVIRONMENT_VARIABLE + ")";
    }
}
