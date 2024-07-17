package org.tframework.core.properties.placeholders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.readers.ReadersFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyPlaceholderResolversFactory {

    /**
     * Creates a list of {@link PropertyPlaceholderResolver}s for the framework to use.
     */
    public static List<PropertyPlaceholderResolver> createDefaultPlaceholderResolvers() {
        var envVarReader = ReadersFactory.createEnvironmentVariableReader();
        return List.of(
                new EnvironmentPlaceholderResolver(envVarReader),
                new FrameworkPropertyPlaceholderResolver()
        );
    }

}
