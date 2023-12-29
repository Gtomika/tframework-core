/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link PropertyFileScanner} that scans only for a default property file
 * {@value DEFAULT_PROPERTY_FILE_NAME}.
 */
@Slf4j
public class DefaultPropertyFileScanner implements PropertyFileScanner {

    public static final String DEFAULT_PROPERTY_FILE_NAME = "properties.yaml";

    @Override
    public Set<String> scan() {
        log.debug("Scanning for default property file: {}", DEFAULT_PROPERTY_FILE_NAME);
        return Set.of(DEFAULT_PROPERTY_FILE_NAME);
    }
}
