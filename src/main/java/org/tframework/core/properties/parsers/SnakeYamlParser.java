/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

/**
 * A {@link YamlParser} implementation that uses the SnakeYaml library. TODO unit test
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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

    //this is not in YamlParsersFactory, that class cannot use any Jackson or SnakeYaml classes.
    /**
     * Creates a {@link SnakeYamlParser}.
     */
    static SnakeYamlParser createSnakeYamlParser() {
        Yaml snakeYaml = new Yaml();
        return new SnakeYamlParser(snakeYaml);
    }
}
