/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.PropertyValue;

class RecursivePropertiesExtractorTest {

    private final RecursivePropertiesExtractor extractor = new RecursivePropertiesExtractor();

    @Test
    public void shouldExtractProperties_fromCommonTestParsedYaml() {
        Map<String, PropertyValue> properties = extractor.extractProperties(ExtractorTestUtils.TEST_PARSED_YAML);

        ExtractorTestUtils.assertPropertyAIsCorrect(properties);
        ExtractorTestUtils.assertPropertyBIsCorrect(properties);
        ExtractorTestUtils.assertPropertyCIsCorrect(properties);
        ExtractorTestUtils.assertPropertyDIsCorrect(properties);
    }

}
