/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

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
}
