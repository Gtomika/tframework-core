/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyParsersFactory {

    /**
     * Creates a default {@link PropertyParser} that the framework can use to parse
     * directly provided properties.
     */
    public static PropertyParser createDefaultPropertyParser() {
        return new DefaultPropertyParser();
    }

}
