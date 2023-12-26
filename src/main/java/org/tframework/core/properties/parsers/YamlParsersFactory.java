/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Creates {@link YamlParser}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlParsersFactory {

    /**
     * Creates a {@link JacksonYamlParser}.
     */
    public static JacksonYamlParser createJacksonYamlParser() {
        return new JacksonYamlParser();
    }

}
