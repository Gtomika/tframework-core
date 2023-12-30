/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
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
 *         Finishes with the property file name. Only one file can be specified. If more is needed,
 *         the argument can be repeated. If this file name is blank, it will be ignored. <b>This is a
 *         relative path inside the {@code resources} folder.</b>
 *     </li>
 * </ul>
 * For example
 * <pre>{@code tframework.propertyFile=custom-properties.yaml}</pre>
 * will be picked up if this file exists in the {@code resources} folder. The file name can
 * be anything, but underneath, it still must be an existing, valid YAML file.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CliArgumentPropertyFileScanner implements PropertyFileScanner {

    public static final String PROPERTY_FILE_ARGUMENT_KEY = "tframework.propertyFile";

    private final String[] args;

    @Override
    public Set<String> scan() {
        return Arrays.stream(args)
                .filter(arg -> CliUtils.isArgumentWithKey(arg, PROPERTY_FILE_ARGUMENT_KEY))
                .map(CliUtils::extractArgumentValue)
                .filter(arg -> !arg.isBlank())
                .peek(arg -> log.debug("Scanning for property file activated by CLI argument: '{}'", arg))
                .collect(Collectors.toSet());
    }
}