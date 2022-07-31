package org.tframework.core.properties;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.flavors.ActiveFlavorManager;
import org.tframework.core.properties.annotations.PropertyMatcherImplementation;
import org.tframework.core.properties.containers.PropertyContainer;
import org.tframework.core.properties.exceptions.PropertyException;
import org.tframework.core.properties.exceptions.PropertyFileNotFoundException;
import org.tframework.core.properties.exceptions.PropertyMatcherException;
import org.tframework.core.properties.matchers.PropertyMatcher;
import org.tframework.core.properties.matchers.StringMatcher;
import org.tframework.core.properties.model.FullPropertiesData;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Finds and reads the properties file. Adds the contents to the {@link PropertyRepository}.
 */
@Slf4j
@RequiredArgsConstructor
public class PropertyScanner {

    /**
     * Name of the environmental variable that can be used to control the location of the properties file.
     */
    private static final String CUSTOM_LOCATION_VAR_NAME = "TFRAMEWORK_CORE_PROPERTIES_LOCATION";

    /**
     * Default path to the properties file of {@value CUSTOM_LOCATION_VAR_NAME} is not provided.
     */
    private static final String DEFAULT_PATH = "/properties.yaml";

    private final ActiveFlavorManager activeFlavorManager;
    private final PropertyRepository propertyRepository;

    /**
     * Finds the properties file and reads the contents, then stores the properties into
     * the {@link #propertyRepository}.
     */
    public void readProperties() {
        log.debug("Starting to read the application properties...");
        URL propertiesFileUrl = findPropertiesFile();
        ObjectMapper yamlMapper = new ObjectMapper(YAMLFactory.builder().build());
        try {
            FullPropertiesData fullPropertiesData = yamlMapper.readValue(propertiesFileUrl, FullPropertiesData.class);
            log.debug("Properties file have been read and is valid. Contains the global properties and properties for the following flavors: {}",
                    fullPropertiesData.getFlavors().keySet());

            List<PropertyMatcher<?>> matchers = detectPropertyMatchers();

            log.debug("Attempting to save the global properties...");
            savePropertiesSection(fullPropertiesData.getGlobal(), matchers);
            for(String flavor: fullPropertiesData.getFlavors().keySet()) {
                if(activeFlavorManager.isActiveFlavor(flavor)) {
                    log.debug("Attempting to save properties for active flavor '{}'...", flavor);
                    savePropertiesSection(fullPropertiesData.getFlavors().get(flavor), matchers);
                } else {
                    log.debug("The properties specified for flavor '{}' will be ignored, as this flavor is not active...", flavor);
                }
            }
            log.info("Properties have been read and stored from file at '{}'", propertiesFileUrl.getPath());
        } catch (DatabindException e) {
            throw new PropertyException("The properties file has invalid contents", e);
        } catch (Exception e) {
            throw new PropertyException("Failed to read properties file", e);
        }
    }

    /**
     * Finds the {@code properties.yaml} file. Checks if the environmental variable with name {@value CUSTOM_LOCATION_VAR_NAME}
     * is provided. If yes, it will check at that location, if no, it will check under {@code src/main/resources} folder.
     * @return The path to the file.
     * @throws PropertyFileNotFoundException If the file can't be located.
     */
    private URL findPropertiesFile() throws PropertyFileNotFoundException {
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

    /**
     * Finds and constructs a list of matchers that are used to convert properties.
     * @throws PropertyMatcherException If there was an exception detecting or constructing the matchers.
     */
    private List<PropertyMatcher<?>> detectPropertyMatchers() throws PropertyMatcherException {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
        );
        log.debug("Scanning for property matcher implementations on the classpath...");
        Set<Class<?>> propertyMatcherClasses = reflections.getTypesAnnotatedWith(PropertyMatcherImplementation.class);
        propertyMatcherClasses.forEach(PropertyValidator::validatePropertyMatcher);
        log.debug("Found {} valid property matcher implementations: {}", propertyMatcherClasses.size(),
                propertyMatcherClasses.stream().map(Class::getName).collect(Collectors.toList()));
        try {
            var matchers = new ArrayList<PropertyMatcher<?>>();
            for(Class<?> matcherClass: propertyMatcherClasses) {
                matchers.add((PropertyMatcher<?>)ConstructorUtils.invokeConstructor(matcherClass));
            }
            //the default matcher that matches everything must be at the end
            matchers.add(ConstructorUtils.invokeConstructor(StringMatcher.class));
            log.debug("Constructed instances for {} property matchers.", matchers.size());
            return matchers;
        } catch (Exception e) {
            throw new PropertyMatcherException("Failed to construct property matcher.", e);
        }
    }

    /**
     * Saves a section (global or flavor) of the properties onto {@link #propertyRepository}.
     * @param section The section to be processed and saved.
     * @param propertyMatchers The {@link PropertyMatcher}s to be used.
     * @throws PropertyException If the properties could not be saved.
     */
    private void savePropertiesSection(Map<String, Object> section, List<PropertyMatcher<?>> propertyMatchers) throws PropertyException {
        for(var propertyEntry: section.entrySet()) {
            String propertyName = propertyEntry.getKey();
            Object rawValue = propertyEntry.getValue();
            PropertyValidator.validatePropertyName(propertyName);
            Property<?> property;
            if(rawValue instanceof Iterable) {
                log.debug("The property '{}' is a list, all elements will be interpreted as strings.", propertyName);
                try {
                    property = createPropertyList(propertyName, (Iterable<String>) rawValue);
                } catch (ClassCastException e) {
                    throw new PropertyException(propertyName, "Probably it has nested list, which is not allowed.", e);
                }
            } else {
                log.debug("The property '{}' is a single value, attempting to use matchers to determine its type.", propertyName);
                property = createProperty(propertyName, (String) rawValue, propertyMatchers);
            }
            propertyRepository.registerProperty(property);
        }
    }

    /**
     * Builds a {@link Property} object from a name and value.
     * @param name Property name.
     * @param rawValue Property value as just a string.
     * @param propertyMatchers Matchers to use in determining property type from {@code rawValue}.
     * @return The property.
     */
    private Property<?> createProperty(String name, String rawValue, List<PropertyMatcher<?>> propertyMatchers) {
        return null;
    }

    /**
     * Build a {@link Property} object from a list of properties.
     * @param name Property name.
     * @param rawElements Raw elements. Will be interpreted as is (strings), no matchers are used.
     * @return The property.
     */
    private Property<List<String>> createPropertyList(String name, Iterable<String> rawElements) {
        return null;
    }


}
