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

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.readers.EnvironmentVariableReader;

/**
 * This {@link PropertyScanner} finds directly specified properties in the environment variables.
 * For a variable to be picked up by this scanner, its name must start with {@value PROPERTY_VARIABLE_PREFIX}.
 *<p>
 * For example, if we need a property {@code my.cool.prop}, then we would need to use
 * this environment variable:
 * <pre>{@code
 * TFRAMEWORK_PROPERTY_my.cool.prop
 * }</pre>
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvironmentPropertyScanner implements PropertyScanner {

    public static final String PROPERTY_VARIABLE_PREFIX = "TFRAMEWORK_PROPERTY_";

    private final EnvironmentVariableReader environmentVariableReader;

    @Override
    public List<String> scanProperties() {
        return environmentVariableReader.getAllVariableNames().stream()
                .filter(variable -> variable.startsWith(PROPERTY_VARIABLE_PREFIX))
                .peek(variable -> log.trace("Found variable '{}' that is candidate for property scanning", variable))
                .map(variable -> {
                    String propertyName = variable.replaceFirst(PROPERTY_VARIABLE_PREFIX, "");
                    String propertyValue = environmentVariableReader.readVariable(variable);
                    return propertyName + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + propertyValue;
                })
                .peek(rawProperty -> log.debug("Found '{}' raw property in environment variables", rawProperty))
                .toList();
    }

    @Override
    public String sourceName() {
        return "Environment Variables (where name starts with " + PROPERTY_VARIABLE_PREFIX + ")";
    }
}
