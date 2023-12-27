/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * A {@link YamlParser} implementation that uses the Jackson library.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonYamlParser implements YamlParser {

    private final ObjectMapper objectMapper;

    /**
     * Parse the YAML using Jackson's {@code ObjectMapper}.
     * @param yaml Valid YAML string.
     */
    @Override
    public Map<String, Object> parseYaml(String yaml) {
        try {
            return objectMapper.readValue(yaml, new TypeReference<>() {});
        } catch (Exception e) {
            throw new YamlParsingException(yaml, e);
        }
    }

    //this is not in YamlParsersFactory, that class cannot use any Jackson or SnakeYaml classes.
    /**
     * Creates a {@link JacksonYamlParser}.
     */
    static JacksonYamlParser createJacksonYamlParser() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return new JacksonYamlParser(objectMapper);
    }
}
