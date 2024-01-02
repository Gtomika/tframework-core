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
import org.tframework.core.properties.parsers.YamlParser;
import org.tframework.core.properties.scanners.PropertyFileScanner;
import org.tframework.core.readers.ResourceFileReader;

@ExtendWith(MockitoExtension.class)
class PropertiesInitializationProcessTest {

    private static final String TEST_PROPERTIES_FILE_1 = "properties.yaml";
    private static final String TEST_PROPERTIES_CONTENT_1 = "whatever1";
    private static final Map<String, Object> TEST_PARSED_PROPERTIES_1 = Map.of(
            "property1", "value1",
            "property2", "value2"
    );
    private static final Map<String, PropertyValue> TEST_EXTRACTED_PROPERTIES_1 = Map.of(
            "property1", new SinglePropertyValue("value1"),
            "property2", new SinglePropertyValue("value2")
    );

    private static final String TEST_PROPERTIES_FILE_2 = "properties-dev.yaml";
    private static final String TEST_PROPERTIES_CONTENT_2 = "whatever2";
    private static final Map<String, Object> TEST_PARSED_PROPERTIES_2 = Map.of(
            "property2", "value2-override",
            "property3", "value3"
    );
    private static final Map<String, PropertyValue> TEST_EXTRACTED_PROPERTIES_2 = Map.of(
            "property2", new SinglePropertyValue("value2-override"),
            "property3", new SinglePropertyValue("value3")
    );

    @Mock
    private PropertyFileScanner propertyFileScanner;

    @Mock
    private ResourceFileReader resourceFileReader;

    @Mock
    private YamlParser yamlParser;

    @Mock
    private PropertiesExtractor propertiesExtractor;

    private PropertiesInitializationProcess propertiesInitializationProcess;

    @BeforeEach
    public void setUp() {
        propertiesInitializationProcess = PropertiesInitializationProcess.builder()
                .resourceFileReader(resourceFileReader)
                .yamlParser(yamlParser)
                .propertiesExtractor(propertiesExtractor)
                .build();
    }

    private void setupMocks() {
        when(propertyFileScanner.scan()).thenReturn(List.of(TEST_PROPERTIES_FILE_1, TEST_PROPERTIES_FILE_2));

        when(resourceFileReader.readResourceFile(TEST_PROPERTIES_FILE_1)).thenReturn(TEST_PROPERTIES_CONTENT_1);
        when(resourceFileReader.readResourceFile(TEST_PROPERTIES_FILE_2)).thenReturn(TEST_PROPERTIES_CONTENT_2);

        when(yamlParser.parseYaml(TEST_PROPERTIES_CONTENT_1)).thenReturn(TEST_PARSED_PROPERTIES_1);
        when(yamlParser.parseYaml(TEST_PROPERTIES_CONTENT_2)).thenReturn(TEST_PARSED_PROPERTIES_2);

        when(propertiesExtractor.extractProperties(TEST_PARSED_PROPERTIES_1)).thenReturn(TEST_EXTRACTED_PROPERTIES_1);
        when(propertiesExtractor.extractProperties(TEST_PARSED_PROPERTIES_2)).thenReturn(TEST_EXTRACTED_PROPERTIES_2);
    }

    private void makeAssertions(PropertiesContainer container) {
        if(container.getPropertyValueObject("property1") instanceof SinglePropertyValue(String value)) {
            assertEquals("value1", value);
        } else {
            fail("property1 is not a SinglePropertyValue");
        }

        if(container.getPropertyValueObject("property2") instanceof SinglePropertyValue(String value)) {
            assertEquals("value2-override", value);
        } else {
            fail("property2 is not a SinglePropertyValue");
        }

        if(container.getPropertyValueObject("property3") instanceof SinglePropertyValue(String value)) {
            assertEquals("value3", value);
        } else {
            fail("property3 is not a SinglePropertyValue");
        }
    }

    @Test
    public void shouldInitializeProperties() {
        setupMocks();
        var propertiesContainer = propertiesInitializationProcess.initialize(List.of(propertyFileScanner));
        makeAssertions(propertiesContainer);
    }

}
