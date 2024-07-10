/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.properties.parsers;

import org.tframework.core.TFrameworkException;

/**
 * Exception thrown when some step of the property parsing process failed.
 */
public class PropertyParsingException extends TFrameworkException {

    private static final String TEMPLATE = """
            Failed to parse raw property value!
            - Raw property: %s
            - Reason: %s
            """;

    public PropertyParsingException(String rawProperty, String reason) {
        super(TEMPLATE.formatted(rawProperty, reason));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
