/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.extractors.leaves.LeafExtractorsFactory;

class RecursivePropertiesExtractorTest {

    private final RecursivePropertiesExtractor extractor = new RecursivePropertiesExtractor(
            LeafExtractorsFactory.createLeafExtractors()
    );

    @Test
    public void shouldExtractProperties_fromCommonTestParsedYaml() {
        var properties = extractor.extractProperties(ExtractorTestUtils.TEST_PARSED_YAML);

        ExtractorTestUtils.assertPropertyAIsCorrect(properties);
        ExtractorTestUtils.assertPropertyBIsCorrect(properties);
        ExtractorTestUtils.assertPropertyCIsCorrect(properties);
        ExtractorTestUtils.assertPropertyDIsCorrect(properties);
    }

}
