/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.filescanners;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
