/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
