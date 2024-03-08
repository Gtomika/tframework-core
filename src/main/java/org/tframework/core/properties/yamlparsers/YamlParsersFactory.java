/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.yamlparsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

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
        log.debug("Using SNAKE YAML parser...");
        return createSnakeYamlParser();
    }

    /**
     * Creates a {@link SnakeYamlParser}.
     */
    static SnakeYamlParser createSnakeYamlParser() {
        Yaml snakeYaml = new Yaml();
        return new SnakeYamlParser(snakeYaml);
    }

}
