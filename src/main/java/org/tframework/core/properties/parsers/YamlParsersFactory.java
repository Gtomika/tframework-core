/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates {@link YamlParser}s.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlParsersFactory {

    /**
     * Creates a {@link YamlParser} that can be used to parse properties files.
     */
    public static YamlParser createDefaultYamlParser() {
        log.debug("Using Jackson YAML parser...");
        return createJacksonYamlParser();
    }

    /**
     * Creates a {@link JacksonYamlParser}.
     */
    static JacksonYamlParser createJacksonYamlParser() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return new JacksonYamlParser(objectMapper);
    }

}
