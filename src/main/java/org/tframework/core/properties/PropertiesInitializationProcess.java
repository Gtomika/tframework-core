/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.tframework.core.properties.extractors.PropertiesExtractor;
import org.tframework.core.properties.filescanners.PropertyFileScanner;
import org.tframework.core.properties.filescanners.PropertyFileScannersFactory;
import org.tframework.core.properties.parsers.PropertyParser;
import org.tframework.core.properties.scanners.PropertyScanner;
import org.tframework.core.properties.scanners.PropertyScannersFactory;
import org.tframework.core.properties.yamlparsers.YamlParser;
import org.tframework.core.readers.ResourceFileReader;
import org.tframework.core.readers.ResourceNotFoundException;

/**
 * This class is responsible for initializing the properties, by the following process:
 * <ul>
 *     <li>Finding the property files to read using {@link PropertyFileScanner}s.</li>
 *     <li>Reading them with a {@link ResourceFileReader}.</li>
 *     <li>Parsing the YAML contents using {@link YamlParser}.</li>
 *     <li>Extracting the properties from the YAML using {@link PropertiesExtractor}.</li>
 * </ul>
 * Properties will be merged together into a {@link PropertiesContainer}. Properties found later will override
 * the ones found earlier, if they have the same name. Use debug level logging and the {@code MDC} to see the
 * overrides.
 * <p>
 * After all property files are found and handled, directly specified properties will be looked up and added.
 * <ul>
 *     <li>{@link org.tframework.core.properties.scanners.PropertyScanner}s detect them.</li>
 *     <li>{@link org.tframework.core.properties.parsers.PropertyParser} will convert the raw properties into {@link Property} objects.</li>
 *     <li>There will be added to the {@link PropertiesContainer}, overriding existing values.</li>
 * </ul>
 */
@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PropertiesInitializationProcess {

    private static final String OVERRIDING_PROPERTY_FILE = "overridingPropertyFile";

    private final ResourceFileReader resourceFileReader;          //reads the property files
    private final YamlParser yamlParser;                          //parses the YAML
    private final PropertiesExtractor propertiesExtractor;        //extracts the properties from the YAML

    private final PropertyParser propertyParser;                  // parser raw property strings

    /**
     * Initializes the properties.
     * @param input {@link PropertiesInitializationInput} with the input parameters.
     * @return {@link PropertiesContainer} containing the properties found.
     */
    public PropertiesContainer initialize(PropertiesInitializationInput input) {
        var propertyFileScanners = PropertyFileScannersFactory.createTframeworkPropertyFileScanners(input);
        var propertyScanners = PropertyScannersFactory.createDefaultPropertyScanners(input);
        return initialize(propertyFileScanners, propertyScanners);
    }

    /**
     * Initializes the properties according to the process documented on the class. This method
     * may be used by tests to perform the initialization with mocked scanners.
     * @param propertyFileScanners {@link PropertyFileScanner}s to find the property files to read.
     * @param propertyScanners {@link PropertyScanner}s to find directly specified properties.
     * @return {@link PropertiesContainer} containing the properties found.
     */
    PropertiesContainer initialize(
            List<PropertyFileScanner> propertyFileScanners,
            List<PropertyScanner> propertyScanners
    ) {
        PropertiesContainer propertiesContainer = PropertiesContainer.empty();

        for(PropertyFileScanner propertyFileScanner: propertyFileScanners) {
            List<String> propertyFiles = propertyFileScanner.scan();
            for(String propertyFile : propertyFiles) {
                log.debug("Attempting to read property file '{}', provided by scanner '{}'", propertyFile, propertyFileScanner.getClass().getName());

                var properties = processPropertyFile(propertyFile);
                if(!properties.isEmpty()) {
                    log.debug("Found {} properties in file '{}', merging them into current properties...", properties.size(), propertyFile);
                    MDC.put(OVERRIDING_PROPERTY_FILE, propertyFile);
                    propertiesContainer = propertiesContainer.merge(properties);
                    MDC.remove(OVERRIDING_PROPERTY_FILE);
                }
            }
        }

        //TODO

        return propertiesContainer;
    }

    private List<Property> processPropertyFile(String propertyFile) {
        try {
            var propertyFileContent = resourceFileReader.readResourceFile(propertyFile);
            var parsedYaml = yamlParser.parseYaml(propertyFileContent);
            return propertiesExtractor.extractProperties(parsedYaml);
        } catch (ResourceNotFoundException e) {
            log.debug("Property file '{}' not found, skipping...", propertyFile);
            return List.of();
        }
    }

}
