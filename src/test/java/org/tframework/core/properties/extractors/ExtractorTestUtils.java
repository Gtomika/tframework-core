/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyUtils;
import org.tframework.core.properties.SinglePropertyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Common test data and methods for {@link PropertiesExtractor} implementations.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ExtractorTestUtils {

    private static final Map<String, Object> TEST_PARSED_YAML_NON_NULL = Map.of(
        "a", "1",
        "b", Map.of(
            "b1", "2",
            "b2", List.of("3", "4")
        )
    );

    //Map.of does not allow null values, so we have to use this workaround
    static final Map<String, Object> TEST_PARSED_YAML = new HashMap<>(TEST_PARSED_YAML_NON_NULL);

    static {
        TEST_PARSED_YAML.put("c", null);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("5");
        strings.add(null);
        TEST_PARSED_YAML.put("d", strings);
    }

    public static void assertPropertyAIsCorrect(List<Property> properties) {
        if(PropertyUtils.getValueFromPropertyList(properties, "a") instanceof SinglePropertyValue spv) {
            assertEquals("1", spv.value());
        } else {
            fail();
        }
    }

    public static void assertPropertyBIsCorrect(List<Property> properties) {
        String b1Name = "b" + PropertiesExtractor.PROPERTY_PATH_SEPARATOR + "b1";
        if(PropertyUtils.getValueFromPropertyList(properties, b1Name) instanceof SinglePropertyValue spv) {
            assertEquals("2", spv.value());
        } else {
            fail();
        }

        String b2Name = "b" + PropertiesExtractor.PROPERTY_PATH_SEPARATOR + "b2";
        if(PropertyUtils.getValueFromPropertyList(properties, b2Name) instanceof ListPropertyValue lpv) {
            List<String> values = lpv.values();
            assertEquals(2, values.size());
            assertEquals("3", values.get(0));
            assertEquals("4", values.get(1));
        } else {
            fail();
        }
    }

    public static void assertPropertyCIsCorrect(List<Property> properties) {
        if(PropertyUtils.getValueFromPropertyList(properties, "c") instanceof SinglePropertyValue spv) {
            assertNull(spv.value());
        } else {
            fail();
        }
    }

    public static void assertPropertyDIsCorrect(List<Property> properties) {
        if(PropertyUtils.getValueFromPropertyList(properties, "d") instanceof ListPropertyValue lpv) {
            List<String> values = lpv.values();
            assertEquals(2, values.size());
            assertEquals("5", values.get(0));
            assertNull(values.get(1));
        } else {
            fail();
        }
    }

}
