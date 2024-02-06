/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.extractors.PropertiesExtractor;
import org.tframework.core.properties.filescanners.PropertyFileScanner;
import org.tframework.core.properties.parsers.PropertyParser;
import org.tframework.core.properties.scanners.PropertyScanner;
import org.tframework.core.properties.yamlparsers.YamlParser;
import org.tframework.core.readers.ResourceFileReader;

@ExtendWith(MockitoExtension.class)
class PropertiesInitializationProcessTest {

    private static final String TEST_PROPERTIES_FILE_1 = "properties.yaml";
    private static final String TEST_PROPERTIES_CONTENT_1 = "whatever1";
    private static final Map<String, Object> TEST_PARSED_PROPERTIES_1 = Map.of(
            "property1", "value1",
            "property2", "value2"
    );
    private static final List<Property> TEST_EXTRACTED_PROPERTIES_1 = List.of(
            new Property("property1", new SinglePropertyValue("value1")),
            new Property("property2", new SinglePropertyValue("value2"))
    );

    private static final String TEST_PROPERTIES_FILE_2 = "properties-dev.yaml";
    private static final String TEST_PROPERTIES_CONTENT_2 = "whatever2";
    private static final Map<String, Object> TEST_PARSED_PROPERTIES_2 = Map.of(
            "property2", "value2-override",
            "property3", "value3"
    );
    private static final List<Property> TEST_EXTRACTED_PROPERTIES_2 = List.of(
            new Property("property2", new SinglePropertyValue("value2-override")),
            new Property("property3", new SinglePropertyValue("value3"))
    );

    private static final List<String> TEST_RAW_PROPERTIES = List.of(
            "property3=value3-override",
            "property4=value4"
    );
    private static final List<Property> TEST_PARSED_DIRECTLY_SPECIFIED_PROPERTIES = List.of(
            new Property("property3", new SinglePropertyValue("value3-override")),
            new Property("property4", new SinglePropertyValue("value4"))
    );

    @Mock
    private PropertyFileScanner propertyFileScanner;

    @Mock
    private PropertyScanner propertyScanner;

    @Mock
    private ResourceFileReader resourceFileReader;

    @Mock
    private YamlParser yamlParser;

    @Mock
    private PropertiesExtractor propertiesExtractor;

    @Mock
    private PropertyParser propertyParser;

    private PropertiesInitializationProcess propertiesInitializationProcess;

    @BeforeEach
    public void setUp() {
        propertiesInitializationProcess = PropertiesInitializationProcess.builder()
                .resourceFileReader(resourceFileReader)
                .yamlParser(yamlParser)
                .propertiesExtractor(propertiesExtractor)
                .propertyParser(propertyParser)
                .build();
    }

    private void setupMocks() {
        //mocking property reading from files ---------------
        when(propertyFileScanner.scan()).thenReturn(List.of(TEST_PROPERTIES_FILE_1, TEST_PROPERTIES_FILE_2));
        when(propertyFileScanner.sourceName()).thenReturn("Mock property file source");

        when(resourceFileReader.readResourceFile(TEST_PROPERTIES_FILE_1)).thenReturn(TEST_PROPERTIES_CONTENT_1);
        when(resourceFileReader.readResourceFile(TEST_PROPERTIES_FILE_2)).thenReturn(TEST_PROPERTIES_CONTENT_2);

        when(yamlParser.parseYaml(TEST_PROPERTIES_CONTENT_1)).thenReturn(TEST_PARSED_PROPERTIES_1);
        when(yamlParser.parseYaml(TEST_PROPERTIES_CONTENT_2)).thenReturn(TEST_PARSED_PROPERTIES_2);

        when(propertiesExtractor.extractProperties(TEST_PARSED_PROPERTIES_1)).thenReturn(TEST_EXTRACTED_PROPERTIES_1);
        when(propertiesExtractor.extractProperties(TEST_PARSED_PROPERTIES_2)).thenReturn(TEST_EXTRACTED_PROPERTIES_2);

        //mocking directly specified property reading -----------------
        when(propertyScanner.scanProperties()).thenReturn(TEST_RAW_PROPERTIES);
        when(propertyScanner.sourceName()).thenReturn("Mock direct source");

        for(int i = 0; i < TEST_RAW_PROPERTIES.size(); i++) {
            when(propertyParser.parseProperty(TEST_RAW_PROPERTIES.get(i))).thenReturn(TEST_PARSED_DIRECTLY_SPECIFIED_PROPERTIES.get(i));
        }
    }

    private void makeAssertions(PropertiesContainer container) {
        if(container.getPropertyValueObject("property1") instanceof SinglePropertyValue(String value)) {
            assertEquals("value1", value);
        } else {
            fail("property1 is not a SinglePropertyValue");
        }

        if(container.getPropertyValueObject("property2") instanceof SinglePropertyValue(String value)) {
            assertEquals("value2-override", value); //property from 2. file overrode the original from 1. file
        } else {
            fail("property2 is not a SinglePropertyValue");
        }

        if(container.getPropertyValueObject("property3") instanceof SinglePropertyValue(String value)) {
            assertEquals("value3-override", value); //property directly specified overrode original from 2. file
        } else {
            fail("property3 is not a SinglePropertyValue");
        }

        if(container.getPropertyValueObject("property4") instanceof SinglePropertyValue(String value)) {
            assertEquals("value4", value);
        } else {
            fail("property4 is not a SinglePropertyValue");
        }
    }

    @Test
    public void shouldInitializeProperties() {
        setupMocks();
        var propertiesContainer = propertiesInitializationProcess.initialize(
                List.of(propertyFileScanner), List.of(propertyScanner)
        );
        makeAssertions(propertiesContainer);
    }

}
