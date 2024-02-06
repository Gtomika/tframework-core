/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.yamlparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * A {@link YamlParser} implementation that uses the Jackson library.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
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

}