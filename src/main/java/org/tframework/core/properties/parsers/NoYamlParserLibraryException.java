/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when the framework cannot find any of the supported YAML parser libraries
 * on the classpath, and cannot construct a {@link YamlParser} as a result.
 */
public class NoYamlParserLibraryException extends TFrameworkException {

    //if updated here, also update YamlParsersFactory method javadoc!
    private static final String TEMPLATE = """
        No YAML parsing library was found on the classpath!
        Include one of the following YAML parsing libraries to your dependencies:
        - Jackson YAML (with Gradle: implementation "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
        - Snake YAML (with Gradle: implementation "org.yaml:snakeyaml:${snakeYamlVersion}")""";

    public NoYamlParserLibraryException() {
        super(TEMPLATE);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
