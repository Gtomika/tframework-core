/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A {@link YamlParser} implementation that uses the Jackson library.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JacksonYamlParser implements YamlParser {

    /**
     * Parse the YAML using Jackson's {@code ObjectMapper}.
     * @param yaml Valid YAML string.
     */
    @Override
    public Map<String, Object> parseYaml(String yaml) {
        return null;
    }
}
