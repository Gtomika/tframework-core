/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.CliUtils;

import java.util.Arrays;
import java.util.List;

/**
 * a {@link PropertyScanner} that finds properties from the CLI arguments. Arguments that
 * start with {@value PROPERTY_ARGUMENT_KEY} followed by {@link org.tframework.core.utils.CliUtils#CLI_KEY_VALUE_SEPARATOR}
 * will be picked up by this scanner. For example, to set a property {@code some.cool.prop} to value {@code 123},
 * we'd need to pass this CLI argument:
 * <pre>{@code
 * tframework.property=some.cool.prop=123
 * }</pre>
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CliArgumentPropertyScanner implements PropertyScanner {

    public static final String PROPERTY_ARGUMENT_KEY = "tframework.property";

    private final String[] args;

    @Override
    public List<String> scanProperties() {
        return Arrays.stream(args)
                .filter(arg -> CliUtils.isArgumentWithKey(arg, PROPERTY_ARGUMENT_KEY))
                .map(CliUtils::extractArgumentValue)
                .peek(rawProperty -> log.debug("Found raw property '{}' in command line arguments", rawProperty))
                .toList();
    }

    @Override
    public String sourceName() {
        return "Command Line Arguments (with key " + PROPERTY_ARGUMENT_KEY + ")";
    }
}
