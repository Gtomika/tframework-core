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
package org.tframework.core.properties.filescanners;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link PropertyFileScanner} that scans only for a default property file
 * {@value DEFAULT_PROPERTY_FILE_NAME}. This file does not need to exist.
 */
@Slf4j
public class DefaultPropertyFileScanner implements PropertyFileScanner {

    public static final String DEFAULT_PROPERTY_FILE_NAME = "properties.yaml";

    @Override
    public List<String> scan() {
        log.debug("Adding default property file: {}", DEFAULT_PROPERTY_FILE_NAME);
        return List.of(DEFAULT_PROPERTY_FILE_NAME);
    }

    @Override
    public String sourceName() {
        return "Default Property File (named " + DEFAULT_PROPERTY_FILE_NAME + ")";
    }
}
