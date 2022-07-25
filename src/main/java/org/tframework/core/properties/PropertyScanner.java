package org.tframework.core.properties;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.exceptions.PropertyFileNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Finds and reads the properties file. Adds the contents to the {@link PropertyRepository}.
 */
@Slf4j
public class PropertyScanner {

    /**
     * Name of the environmental variable that can be used to control the location of the properties file.
     */
    private static final String CUSTOM_LOCATION_VAR_NAME = "TFRAMEWORK_CORE_PROPERTIES_LOCATION";

    /**
     * Default path to the properties file of {@value CUSTOM_LOCATION_VAR_NAME} is not provided.
     */
    private static final String DEFAULT_PATH = "/properties.yaml";

    /**
     * Finds the {@code properties.yaml} file. Checks if the environmental variable with name {@value CUSTOM_LOCATION_VAR_NAME}
     * is provided. If yes, it will check at that location, if no, it will check under {@code src/main/resources} folder.
     * @return The path to the file.
     * @throws PropertyFileNotFoundException If the file can't be located.
     */
    public URL findPropertiesFile() throws PropertyFileNotFoundException {
        String customLocation = System.getenv(CUSTOM_LOCATION_VAR_NAME);
        if(customLocation != null) {
            log.debug("Environmental variable '{}' set, looking for properties file at '{}'.", CUSTOM_LOCATION_VAR_NAME, customLocation);
            try {
                Path path = Paths.get(customLocation);
                if(Files.notExists(path)) {
                    log.error("Properties file is not found at custom location '{}'.", customLocation);
                    throw new PropertyFileNotFoundException(String.format("Properties file is not found at custom location '%s'.", customLocation));
                }
                log.info("The properties file has been found at the custom location '{}'", customLocation);
                return path.toUri().toURL();
            } catch (MalformedURLException | IllegalArgumentException e) {
                log.error("The provided value '{}' is not a valid absolute file system path.", customLocation, e);
                throw new PropertyFileNotFoundException(String.format("The provided custom location '%s' is not a valid absolute file system path.", customLocation));
            }
        } else {
            log.debug("Environmental variable '{}' not specified, looking for properties.yaml at the default location: 'resources/properties.yaml'", CUSTOM_LOCATION_VAR_NAME);
            URL url = PropertyScanner.class.getClassLoader().getResource(DEFAULT_PATH);
            if(url == null) {
                log.error("Failed to find properties.yaml at expected default location: 'resources/properties.yaml'");
                throw new PropertyFileNotFoundException("Failed to find 'properties.yaml' file, expected it at 'resources/properties.yaml'. " +
                        "Place the file there or specify a custom location with environmental variable '" + CUSTOM_LOCATION_VAR_NAME + "'.");
            }
            log.info("The properties file has been found at the default location");
            return url;
        }
    }

}
