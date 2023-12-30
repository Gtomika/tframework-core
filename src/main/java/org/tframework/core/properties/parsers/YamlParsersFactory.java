/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.OptionalDependency;
import org.tframework.core.utils.OptionalDependencyUtils;

//this class must not import any Jackson or SnakeYaml classes!!!
/**
 * Creates {@link YamlParser}s.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlParsersFactory {

    //if list is updated here, also update NoYamlParserLibraryException TEMPLATE!
    /**
     * Creates a {@link YamlParser} that is available, based on the classpath.
     * Since the YAML parsing libraries are optional, we don't know which one will be included by the user:
     * this method checks for the included libraries on the classpath, and creates an available parser. The following
     * libraries are available:
     * <ul>
     *     <li>Jackson YAML module.</li>
     *     <li>Snake YAML.</li>
     * </ul>
     * @throws NoYamlParserLibraryException If none of the supported YAML parsing libraries were available.
     */
    public static YamlParser createAvailableYamlParser() {
        if(OptionalDependencyUtils.isOptionalDependencyAvailable(OptionalDependency.JACKSON_YAML)) {
            log.info("Found Jackson YAML library on the classpath, using '{}'", JacksonYamlParser.class.getName());
            return JacksonYamlParser.createJacksonYamlParser();
        }

        if(OptionalDependencyUtils.isOptionalDependencyAvailable(OptionalDependency.SNAKE_YAML)) {
            log.info("Found Snake YAML library on the classpath, using '{}'", SnakeYamlParser.class.getName());
            return SnakeYamlParser.createSnakeYamlParser();
        }

        log.error("No YAML parsing library was found on the classpath");
        throw new NoYamlParserLibraryException();
    }

}
