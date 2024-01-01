/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

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

    public static void assertPropertyAIsCorrect(Map<String, PropertyValue> properties) {
        if(properties.get("a") instanceof SinglePropertyValue spv) {
            assertEquals("1", spv.value());
        } else {
            fail();
        }
    }

    public static void assertPropertyBIsCorrect(Map<String, PropertyValue> properties) {
        if(properties.get("b" + PropertiesExtractor.PROPERTY_PATH_SEPARATOR + "b1") instanceof SinglePropertyValue spv) {
            assertEquals("2", spv.value());
        } else {
            fail();
        }

        if(properties.get("b" + PropertiesExtractor.PROPERTY_PATH_SEPARATOR + "b2") instanceof ListPropertyValue lpv) {
            List<String> values = lpv.values();
            assertEquals(2, values.size());
            assertEquals("3", values.get(0));
            assertEquals("4", values.get(1));
        } else {
            fail();
        }
    }

    public static void assertPropertyCIsCorrect(Map<String, PropertyValue> properties) {
        if(properties.get("c") instanceof SinglePropertyValue spv) {
            assertNull(spv.value());
        } else {
            fail();
        }
    }

    public static void assertPropertyDIsCorrect(Map<String, PropertyValue> properties) {
        if(properties.get("d") instanceof ListPropertyValue lpv) {
            List<String> values = lpv.values();
            assertEquals(2, values.size());
            assertEquals("5", values.get(0));
            assertNull(values.get(1));
        } else {
            fail();
        }
    }

}
