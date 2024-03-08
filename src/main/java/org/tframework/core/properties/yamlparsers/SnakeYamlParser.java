/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.yamlparsers;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

/**
 * A {@link YamlParser} implementation that uses the {@code SnakeYaml} library.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SnakeYamlParser implements YamlParser {

    private final Yaml snakeYaml;

    @Override
    public Map<String, Object> parseYaml(String yaml) {
        try {
            return snakeYaml.load(yaml);
        } catch (Exception e) {
            throw new YamlParsingException(yaml, e);
        }
    }
}
