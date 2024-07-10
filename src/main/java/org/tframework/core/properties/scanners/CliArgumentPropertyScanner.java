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
package org.tframework.core.properties.scanners;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.CliUtils;

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
