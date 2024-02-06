/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.filescanners;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.CliUtils;

/**
 * A {@link PropertyFileScanner} that scans for property files specified as command line arguments.
 * The arguments that will be picked up must have the following format:
 * <ul>
 *     <li>Starts with {@value PROPERTY_FILE_ARGUMENT_KEY}.</li>
 *     <li>Continues with {@value CliUtils#CLI_KEY_VALUE_SEPARATOR}.</li>
 *     <li>
 *         Finishes with the property file names. Multiple can be provided with comma separation. <b>These are
 *         relative paths inside the {@code resources} folder.</b>
 *     </li>
 * </ul>
 * For example
 * <pre>{@code tframework.propertyFile=custom-properties.yaml}</pre>
 * will be picked up if this file exists in the {@code resources} folder. The file name can
 * be anything, but underneath, it still should be an existing, valid YAML file.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CliArgumentPropertyFileScanner implements PropertyFileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String PROPERTY_FILE_ARGUMENT_KEY = "tframework.propertyFiles";

    private final String[] args;

    @Override
    public List<String> scan() {
        return Arrays.stream(args)
                .filter(arg -> CliUtils.isArgumentWithKey(arg, PROPERTY_FILE_ARGUMENT_KEY))
                .map(CliUtils::extractArgumentValue)
                .filter(arg -> !arg.isBlank())
                .flatMap(arg -> Arrays.stream(arg.split(",")))
                .map(String::trim)
                .peek(arg -> log.debug("Adding property file activated by CLI argument: '{}'", arg))
                .toList();
    }

    @Override
    public String sourceName() {
        return "Command Line Arguments (with key " + PROPERTY_FILE_ARGUMENT_KEY + ")";
    }
}
