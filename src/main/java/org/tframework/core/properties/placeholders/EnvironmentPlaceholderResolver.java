package org.tframework.core.properties.placeholders;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This placeholder resolver looks up values from the environment variables. It will find
 * substrings in the value that are in the format of {@code env{VAR_NAME}} and replace them with the
 * value of the environment variable {@code VAR_NAME}. We can specify the default value if the
 * environment variable is not set by using the format {@code env{VAR_NAME|default_value}}.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvironmentPlaceholderResolver implements PropertyPlaceholderResolver {

    private static final Pattern ENV_PATTERN = Pattern.compile("env\\{(\\w+)(\\|(\\w+))?}");

    private final EnvironmentVariableReader environmentVariableReader;

    @Override
    public String resolvePlaceholders(String propertyValue, PropertiesContainer propertiesContainer) {
        StringBuilder resolvedValue = new StringBuilder();
        Matcher matcher = ENV_PATTERN.matcher(propertyValue);

        while(matcher.find()) {
            String envVarName = matcher.group(1);
            String envVarDefaultValue = matcher.group(3); // This will be null if the default value is not specified
            log.debug("Found environment variable placeholder in property value '{}'. Variable: {}," +
                            " default value: {}", propertyValue, envVarName, envVarDefaultValue);

            String resolvedVar = resolveVariable(envVarName, envVarDefaultValue);
            matcher.appendReplacement(resolvedValue, resolvedVar);
            log.debug("Resolved environment variable '{}' placeholder to '{}'", envVarName, resolvedVar);
        }

        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }

    private String resolveVariable(String envVarName, String defaultValue) {
        try {
            return environmentVariableReader.readVariable(envVarName);
        } catch (EnvironmentVariableNotFoundException e) {
            if(defaultValue != null) {
                log.debug("Environment variable {} not found, using default value: {}", envVarName, defaultValue);
                return defaultValue;
            } else {
                throw e;
            }
        }
    }
}
