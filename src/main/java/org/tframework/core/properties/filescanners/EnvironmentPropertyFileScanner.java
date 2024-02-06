/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.filescanners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

import java.util.Arrays;
import java.util.List;

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
