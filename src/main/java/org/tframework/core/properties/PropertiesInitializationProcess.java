/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.tframework.core.properties.extractors.PropertiesExtractor;
import org.tframework.core.properties.parsers.YamlParser;
import org.tframework.core.properties.scanners.PropertyFileScanner;
import org.tframework.core.readers.ResourceFileReader;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PropertiesInitializationProcess {

    private static final String SOURCE_FILE = "sourceFile";
    private static final String OVERRIDE_FILE = "overrideFile";

    private final List<PropertyFileScanner> propertyFileScanners; //finds the property files
    private final ResourceFileReader resourceFileReader;          //reads the property files
    private final YamlParser yamlParser;                          //parses the YAML
    private final PropertiesExtractor propertiesExtractor;        //extracts the properties from the YAML

    public PropertiesContainer initialize() {
        PropertiesContainer propertiesContainer = PropertiesContainer.empty();

        for(PropertyFileScanner propertyFileScanner: propertyFileScanners) {
            List<String> propertyFiles = propertyFileScanner.scan();
            for(String propertyFile : propertyFiles) {
                MDC.put(OVERRIDE_FILE, propertyFile);
                log.debug("Found property file '{}', provided by scanner '{}'", propertyFile, propertyFileScanner.getClass().getName());

                var propertyFileContent = resourceFileReader.readResourceFile(propertyFile);
                var parsedYaml = yamlParser.parseYaml(propertyFileContent);
                var properties = propertiesExtractor.extractProperties(parsedYaml);

                log.debug("Found {} properties in file '{}', merging...", properties.size(), propertyFile);
                propertiesContainer = propertiesContainer.merge(properties);
                MDC.put(SOURCE_FILE, propertyFile);
            }
        }

        MDC.remove(SOURCE_FILE);
        MDC.remove(OVERRIDE_FILE);
        return propertiesContainer;
    }

}
